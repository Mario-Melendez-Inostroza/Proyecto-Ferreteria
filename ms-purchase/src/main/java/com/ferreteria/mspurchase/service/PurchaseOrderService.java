package com.ferreteria.mspurchase.service;

import com.ferreteria.mspurchase.dto.PurchaseOrderRequestDto;
import com.ferreteria.mspurchase.dto.PurchaseOrderResponseDto;

import java.util.List;
import java.util.UUID;

public interface PurchaseOrderService {

    /**
     * Creates a new purchase order in PENDING status, validating that the
     * supplier and every referenced product exist before persisting it.
     *
     * @param dto the purchase order data to create
     * @return the created purchase order
     */
    PurchaseOrderResponseDto crear(PurchaseOrderRequestDto dto);

    /**
     * Lists all purchase orders ordered by creation date, most recent first.
     *
     * @return the list of purchase orders
     */
    List<PurchaseOrderResponseDto> listarTodas();

    /**
     * Finds a purchase order by its identifier.
     *
     * @param id the purchase order unique identifier
     * @return the matching purchase order
     */
    PurchaseOrderResponseDto buscarPorId(UUID id);

    /**
     * Confirms a PENDING purchase order: increases stock in ms-inventory for
     * every item and marks the order as CONFIRMED. If any stock update call
     * fails, the whole confirmation is aborted (no partial state change is
     * persisted) so the order remains PENDING and can be retried.
     *
     * @param id the purchase order unique identifier
     * @return the confirmed purchase order
     */
    PurchaseOrderResponseDto confirmar(UUID id);

    /**
     * Cancels a PENDING purchase order.
     *
     * @param id the purchase order unique identifier
     * @return the cancelled purchase order
     */
    PurchaseOrderResponseDto cancelar(UUID id);
}
