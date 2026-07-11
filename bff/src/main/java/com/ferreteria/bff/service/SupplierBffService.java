package com.ferreteria.bff.service;

import com.ferreteria.bff.client.SupplierClient;
import com.ferreteria.bff.dto.MessageResponseDto;
import com.ferreteria.bff.dto.SupplierRequestDto;
import com.ferreteria.bff.dto.SupplierResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * BFF service for supplier operations. Contains no business logic of its
 * own; it simply delegates each call to {@link SupplierClient}, the HTTP
 * client for the supplier microservice.
 */
@Service
@RequiredArgsConstructor
public class SupplierBffService {

    private final SupplierClient supplierClient;

    /**
     * Delegates the retrieval of all active suppliers to the supplier
     * microservice.
     *
     * @return the list of active suppliers
     */
    public List<SupplierResponseDto> findAll() {
        return supplierClient.findAll();
    }

    /**
     * Delegates the lookup of a supplier by its identifier to the supplier
     * microservice.
     *
     * @param id the supplier unique identifier
     * @return the matching supplier
     */
    public SupplierResponseDto findById(UUID id) {
        return supplierClient.findById(id);
    }

    /**
     * Delegates the search of suppliers by name to the supplier
     * microservice.
     *
     * @param name the supplier name search value
     * @return the list of matching suppliers
     */
    public List<SupplierResponseDto> findByName(String name) {
        return supplierClient.findByName(name);
    }

    /**
     * Delegates the creation of a supplier to the supplier microservice.
     *
     * @param request the supplier data to create
     * @return the created supplier
     */
    public SupplierResponseDto create(SupplierRequestDto request) {
        return supplierClient.create(request);
    }

    /**
     * Delegates the update of a supplier to the supplier microservice.
     *
     * @param id the supplier unique identifier
     * @param request the updated supplier data
     * @return the updated supplier
     */
    public SupplierResponseDto update(UUID id, SupplierRequestDto request) {
        return supplierClient.update(id, request);
    }

    /**
     * Delegates the deactivation of a supplier to the supplier
     * microservice.
     *
     * @param id the supplier unique identifier
     * @return a confirmation message
     */
    public MessageResponseDto deactivate(UUID id) {
        return supplierClient.deactivate(id);
    }
}
