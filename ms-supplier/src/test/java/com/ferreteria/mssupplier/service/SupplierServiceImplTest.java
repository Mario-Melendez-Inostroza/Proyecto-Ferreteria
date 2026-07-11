package com.ferreteria.mssupplier.service;

import com.ferreteria.mssupplier.dto.MessageResponseDto;
import com.ferreteria.mssupplier.dto.SupplierRequestDto;
import com.ferreteria.mssupplier.dto.SupplierResponseDto;
import com.ferreteria.mssupplier.exception.SupplierAlreadyExistsException;
import com.ferreteria.mssupplier.exception.SupplierNotFoundException;
import com.ferreteria.mssupplier.model.Supplier;
import com.ferreteria.mssupplier.repository.SupplierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Test
    void crearShouldSaveSupplier() {
        SupplierServiceImpl service = new SupplierServiceImpl(supplierRepository);
        SupplierRequestDto dto = new SupplierRequestDto("Ferretería El Tornillo", "76.111.222-3", "Juan Pérez",
                "+56 9 1111 2222", "contacto@eltornillo.cl", "Av. Industrial 450");
        when(supplierRepository.existsByRut(dto.rut())).thenReturn(false);
        when(supplierRepository.save(any(Supplier.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SupplierResponseDto result = service.crear(dto);

        assertNotNull(result);
        assertEquals("Ferretería El Tornillo", result.name());
        assertEquals(Boolean.TRUE, result.active());
        verify(supplierRepository).save(any(Supplier.class));
    }

    @Test
    void crearShouldThrowWhenRutAlreadyExists() {
        SupplierServiceImpl service = new SupplierServiceImpl(supplierRepository);
        SupplierRequestDto dto = new SupplierRequestDto("Ferretería El Tornillo", "76.111.222-3", "Juan Pérez",
                "+56 9 1111 2222", "contacto@eltornillo.cl", "Av. Industrial 450");
        when(supplierRepository.existsByRut(dto.rut())).thenReturn(true);

        SupplierAlreadyExistsException ex = assertThrows(SupplierAlreadyExistsException.class,
                () -> service.crear(dto));

        assertNotNull(ex.getMessage());
        verify(supplierRepository, never()).save(any());
    }

    @Test
    void buscarPorIdShouldThrowWhenNotFound() {
        SupplierServiceImpl service = new SupplierServiceImpl(supplierRepository);
        UUID id = UUID.randomUUID();
        when(supplierRepository.findById(id)).thenReturn(Optional.empty());

        SupplierNotFoundException ex = assertThrows(SupplierNotFoundException.class,
                () -> service.buscarPorId(id));

        assertNotNull(ex.getMessage());
    }

    @Test
    void buscarPorIdShouldReturnWhenExists() {
        SupplierServiceImpl service = new SupplierServiceImpl(supplierRepository);
        UUID id = UUID.randomUUID();
        Supplier supplier = Supplier.builder().id(id).name("Distribuidora Maderas").rut("77.222.333-4")
                .active(true).createdAt(LocalDateTime.now()).build();
        when(supplierRepository.findById(id)).thenReturn(Optional.of(supplier));

        SupplierResponseDto result = service.buscarPorId(id);

        assertEquals(id, result.id());
        assertEquals("Distribuidora Maderas", result.name());
    }

    @Test
    void listarActivosShouldReturnAllActiveSuppliers() {
        SupplierServiceImpl service = new SupplierServiceImpl(supplierRepository);
        Supplier supplier = Supplier.builder().id(UUID.randomUUID()).name("Importadora Andina")
                .rut("78.333.444-5").active(true).createdAt(LocalDateTime.now()).build();
        when(supplierRepository.findByActiveTrue()).thenReturn(List.of(supplier));

        List<SupplierResponseDto> result = service.listarActivos();

        assertEquals(1, result.size());
        assertEquals("Importadora Andina", result.get(0).name());
    }

    @Test
    void buscarPorNombreShouldReturnMatchingSuppliers() {
        SupplierServiceImpl service = new SupplierServiceImpl(supplierRepository);
        Supplier supplier = Supplier.builder().id(UUID.randomUUID()).name("Ferretería El Tornillo")
                .rut("76.111.222-3").active(true).createdAt(LocalDateTime.now()).build();
        when(supplierRepository.findByNameContainingIgnoreCaseAndActiveTrue("Tornillo"))
                .thenReturn(List.of(supplier));

        List<SupplierResponseDto> result = service.buscarPorNombre("Tornillo");

        assertEquals(1, result.size());
    }

    @Test
    void actualizarShouldUpdateFields() {
        SupplierServiceImpl service = new SupplierServiceImpl(supplierRepository);
        UUID id = UUID.randomUUID();
        Supplier supplier = Supplier.builder().id(id).name("Ferretería El Tornillo").rut("76.111.222-3")
                .active(true).createdAt(LocalDateTime.now()).build();
        SupplierRequestDto dto = new SupplierRequestDto("Ferretería El Tornillo Ltda.", "76.111.222-3",
                "Juan Pérez", "+56 9 1111 2222", "contacto@eltornillo.cl", "Av. Industrial 450");
        when(supplierRepository.findById(id)).thenReturn(Optional.of(supplier));
        when(supplierRepository.save(any(Supplier.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SupplierResponseDto result = service.actualizar(id, dto);

        assertEquals("Ferretería El Tornillo Ltda.", result.name());
        assertEquals("Juan Pérez", result.contactName());
    }

    @Test
    void actualizarShouldThrowWhenNotFound() {
        SupplierServiceImpl service = new SupplierServiceImpl(supplierRepository);
        UUID id = UUID.randomUUID();
        SupplierRequestDto dto = new SupplierRequestDto("Ferretería El Tornillo", "76.111.222-3", "Juan Pérez",
                "+56 9 1111 2222", "contacto@eltornillo.cl", "Av. Industrial 450");
        when(supplierRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(SupplierNotFoundException.class, () -> service.actualizar(id, dto));
    }

    @Test
    void desactivarShouldSetActiveFalse() {
        SupplierServiceImpl service = new SupplierServiceImpl(supplierRepository);
        UUID id = UUID.randomUUID();
        Supplier supplier = Supplier.builder().id(id).name("Ferretería El Tornillo").rut("76.111.222-3")
                .active(true).createdAt(LocalDateTime.now()).build();
        when(supplierRepository.findById(id)).thenReturn(Optional.of(supplier));
        when(supplierRepository.save(any(Supplier.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MessageResponseDto result = service.desactivar(id);

        assertNotNull(result.message());
        assertFalse(supplier.getActive());
    }

    @Test
    void desactivarShouldThrowWhenNotFound() {
        SupplierServiceImpl service = new SupplierServiceImpl(supplierRepository);
        UUID id = UUID.randomUUID();
        when(supplierRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(SupplierNotFoundException.class, () -> service.desactivar(id));
    }
}
