package com.ferreteria.mspurchase.controller;

import com.ferreteria.mspurchase.dto.PurchaseOrderRequestDto;
import com.ferreteria.mspurchase.dto.PurchaseOrderResponseDto;
import com.ferreteria.mspurchase.service.PurchaseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller exposing purchase order management: creation, listing,
 * lookup, confirmation and cancellation of orders placed to suppliers.
 */
@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
@Tag(name = "Purchases", description = "Endpoints for supplier purchase order management")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    /**
     * Creates a purchase order in PENDING status.
     *
     * @param dto the purchase order data to create
     * @return the created purchase order with HTTP 201 status
     */
    @Operation(
            summary = "Create purchase order",
            description = "Creates a purchase order for a supplier in PENDING status.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Purchase order data to create",
                    required = true
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Purchase order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid purchase order request"),
            @ApiResponse(responseCode = "404", description = "Supplier or product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<PurchaseOrderResponseDto> crear(@Valid @RequestBody PurchaseOrderRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseOrderService.crear(dto));
    }

    /**
     * Lists all purchase orders ordered by creation date, most recent first.
     *
     * @return the list of purchase orders
     */
    @Operation(
            summary = "List purchase orders",
            description = "Returns all purchase orders ordered by creation date, most recent first."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase orders retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<PurchaseOrderResponseDto>> listarTodas() {
        return ResponseEntity.ok(purchaseOrderService.listarTodas());
    }

    /**
     * Finds a purchase order by its identifier.
     *
     * @param id the purchase order unique identifier
     * @return the matching purchase order
     */
    @Operation(
            summary = "Find purchase order by id",
            description = "Returns the purchase order identified by the given id."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase order found"),
            @ApiResponse(responseCode = "404", description = "Purchase order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderResponseDto> buscarPorId(
            @Parameter(description = "Purchase order unique identifier", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(purchaseOrderService.buscarPorId(id));
    }

    /**
     * Confirms a PENDING purchase order and increases stock in ms-inventory
     * for every item.
     *
     * @param id the purchase order unique identifier
     * @return the confirmed purchase order
     */
    @Operation(
            summary = "Confirm purchase order",
            description = "Confirms a PENDING purchase order and increases stock for every item."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase order confirmed successfully"),
            @ApiResponse(responseCode = "400", description = "Purchase order is not in PENDING status"),
            @ApiResponse(responseCode = "404", description = "Purchase order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/confirm")
    public ResponseEntity<PurchaseOrderResponseDto> confirmar(
            @Parameter(description = "Purchase order unique identifier", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(purchaseOrderService.confirmar(id));
    }

    /**
     * Cancels a PENDING purchase order.
     *
     * @param id the purchase order unique identifier
     * @return the cancelled purchase order
     */
    @Operation(
            summary = "Cancel purchase order",
            description = "Cancels a PENDING purchase order."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase order cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Purchase order is not in PENDING status"),
            @ApiResponse(responseCode = "404", description = "Purchase order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/cancel")
    public ResponseEntity<PurchaseOrderResponseDto> cancelar(
            @Parameter(description = "Purchase order unique identifier", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(purchaseOrderService.cancelar(id));
    }
}
