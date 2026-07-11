package com.ferreteria.bff.service;

import com.ferreteria.bff.client.PurchaseClient;
import com.ferreteria.bff.dto.PurchaseOrderRequestDto;
import com.ferreteria.bff.dto.PurchaseOrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * BFF service for purchase order operations. Contains no business logic of
 * its own; it simply delegates each call to {@link PurchaseClient}, the HTTP
 * client for the purchase microservice.
 */
@Service
@RequiredArgsConstructor
public class PurchaseBffService {

    private final PurchaseClient purchaseClient;

    /**
     * Delegates the retrieval of all purchase orders to the purchase
     * microservice.
     *
     * @return the list of purchase orders
     */
    public List<PurchaseOrderResponseDto> findAll() {
        return purchaseClient.findAll();
    }

    /**
     * Delegates the lookup of a purchase order by its identifier to the
     * purchase microservice.
     *
     * @param id the purchase order unique identifier
     * @return the matching purchase order
     */
    public PurchaseOrderResponseDto findById(UUID id) {
        return purchaseClient.findById(id);
    }

    /**
     * Delegates the creation of a purchase order to the purchase
     * microservice.
     *
     * @param request the purchase order data to create
     * @return the created purchase order
     */
    public PurchaseOrderResponseDto create(PurchaseOrderRequestDto request) {
        return purchaseClient.create(request);
    }

    /**
     * Delegates the confirmation of a purchase order to the purchase
     * microservice.
     *
     * @param id the purchase order unique identifier
     * @return the confirmed purchase order
     */
    public PurchaseOrderResponseDto confirm(UUID id) {
        return purchaseClient.confirm(id);
    }

    /**
     * Delegates the cancellation of a purchase order to the purchase
     * microservice.
     *
     * @param id the purchase order unique identifier
     * @return the cancelled purchase order
     */
    public PurchaseOrderResponseDto cancel(UUID id) {
        return purchaseClient.cancel(id);
    }
}
