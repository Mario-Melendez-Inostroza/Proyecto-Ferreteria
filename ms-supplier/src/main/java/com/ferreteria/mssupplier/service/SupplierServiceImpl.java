package com.ferreteria.mssupplier.service;

import com.ferreteria.mssupplier.dto.*;
import com.ferreteria.mssupplier.exception.SupplierAlreadyExistsException;
import com.ferreteria.mssupplier.exception.SupplierNotFoundException;
import com.ferreteria.mssupplier.model.Supplier;
import com.ferreteria.mssupplier.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Default {@link SupplierService} implementation backed by
 * {@link SupplierRepository} for persistence of suppliers.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    private SupplierResponseDto toDto(Supplier s) {
        return new SupplierResponseDto(s.getId(), s.getName(), s.getRut(), s.getContactName(),
                s.getPhone(), s.getEmail(), s.getAddress(), s.getActive(), s.getCreatedAt());
    }

    /**
     * Lists all active suppliers.
     *
     * @return the list of active suppliers
     */
    @Override
    public List<SupplierResponseDto> listarActivos() {
        return supplierRepository.findByActiveTrue().stream().map(this::toDto).toList();
    }

    /**
     * Finds a supplier by its unique identifier.
     *
     * @param id the supplier unique identifier
     * @return the matching supplier
     */
    @Override
    public SupplierResponseDto buscarPorId(UUID id) {
        return toDto(supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Proveedor no encontrado: " + id)));
    }

    /**
     * Searches active suppliers whose names contain the given value.
     *
     * @param nombre the supplier name search value
     * @return the list of matching active suppliers
     */
    @Override
    public List<SupplierResponseDto> buscarPorNombre(String nombre) {
        return supplierRepository.findByNameContainingIgnoreCaseAndActiveTrue(nombre)
                .stream().map(this::toDto).toList();
    }

    /**
     * Creates a new supplier, validating that its RUT is unique.
     *
     * @param dto the supplier data to create
     * @return the newly created supplier
     */
    @Override
    public SupplierResponseDto crear(SupplierRequestDto dto) {
        if (supplierRepository.existsByRut(dto.rut())) {
            log.warn("Intento de crear proveedor duplicado con RUT: {}", dto.rut());
            throw new SupplierAlreadyExistsException("El proveedor ya existe con RUT: " + dto.rut());
        }
        Supplier saved = supplierRepository.save(Supplier.builder()
                .name(dto.name())
                .rut(dto.rut())
                .contactName(dto.contactName())
                .phone(dto.phone())
                .email(dto.email())
                .address(dto.address())
                .active(true)
                .build());
        log.info("Proveedor creado: {} ({})", saved.getName(), saved.getId());
        return toDto(saved);
    }

    /**
     * Updates an existing supplier identified by its unique identifier.
     *
     * @param id  the supplier unique identifier
     * @param dto the updated supplier data
     * @return the updated supplier
     */
    @Override
    public SupplierResponseDto actualizar(UUID id, SupplierRequestDto dto) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Proveedor no encontrado: " + id));
        supplier.setName(dto.name());
        supplier.setRut(dto.rut());
        supplier.setContactName(dto.contactName());
        supplier.setPhone(dto.phone());
        supplier.setEmail(dto.email());
        supplier.setAddress(dto.address());
        Supplier updated = supplierRepository.save(supplier);
        log.info("Proveedor actualizado: {} ({})", updated.getName(), updated.getId());
        return toDto(updated);
    }

    /**
     * Deactivates an existing supplier by its unique identifier. This is a
     * soft delete: the record is not physically removed.
     *
     * @param id the supplier unique identifier
     * @return a confirmation message
     */
    @Override
    public MessageResponseDto desactivar(UUID id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Proveedor no encontrado: " + id));
        supplier.setActive(false);
        supplierRepository.save(supplier);
        return new MessageResponseDto("Proveedor desactivado: " + supplier.getName());
    }
}
