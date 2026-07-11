package com.ferreteria.mssupplier.service;

import com.ferreteria.mssupplier.dto.*;
import java.util.List;
import java.util.UUID;

/**
 * Business operations for browsing, creating, updating, and deactivating
 * suppliers of the hardware store.
 */
public interface SupplierService {

    /**
     * Lists all active suppliers.
     *
     * @return the list of active suppliers
     */
    List<SupplierResponseDto> listarActivos();

    /**
     * Finds a supplier by its unique identifier.
     *
     * @param id the supplier unique identifier
     * @return the matching supplier
     */
    SupplierResponseDto buscarPorId(UUID id);

    /**
     * Searches active suppliers whose names contain the given value.
     *
     * @param nombre the supplier name search value
     * @return the list of matching active suppliers
     */
    List<SupplierResponseDto> buscarPorNombre(String nombre);

    /**
     * Creates a new supplier, validating that its RUT is unique.
     *
     * @param dto the supplier data to create
     * @return the newly created supplier
     */
    SupplierResponseDto crear(SupplierRequestDto dto);

    /**
     * Updates an existing supplier identified by its unique identifier.
     *
     * @param id  the supplier unique identifier
     * @param dto the updated supplier data
     * @return the updated supplier
     */
    SupplierResponseDto actualizar(UUID id, SupplierRequestDto dto);

    /**
     * Deactivates an existing supplier by its unique identifier. This is a
     * soft delete: the record is not physically removed.
     *
     * @param id the supplier unique identifier
     * @return a confirmation message
     */
    MessageResponseDto desactivar(UUID id);
}
