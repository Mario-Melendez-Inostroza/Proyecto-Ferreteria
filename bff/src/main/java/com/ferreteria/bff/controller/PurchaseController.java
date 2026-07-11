package com.ferreteria.bff.controller;

import com.ferreteria.bff.dto.PurchaseOrderRequestDto;
import com.ferreteria.bff.dto.PurchaseOrderResponseDto;
import com.ferreteria.bff.service.PurchaseBffService;
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
 * BFF controller exposing purchase order management endpoints to the
 * frontend, delegating all operations to the purchase microservice through
 * {@link PurchaseBffService}.
 */
@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
@Tag(name = "Purchases", description = "Endpoints for supplier purchase order management")
public class PurchaseController {

    private final PurchaseBffService purchaseBffService;

    /**
     * Returns all purchase orders available through the purchase service.
     *
     * @return the list of purchase orders
     */
    @Operation(
            summary = "List purchase orders",
            description = "Returns all purchase orders available through the purchase service."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase orders retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<PurchaseOrderResponseDto>> findAll() {
        return ResponseEntity.ok(purchaseBffService.findAll());
    }

    /**
     * Returns a purchase order that matches the provided identifier.
     *
     * @param id the purchase order unique identifier
     * @return the matching purchase order
     */
    @Operation(
            summary = "Find purchase order by ID",
            description = "Returns a purchase order that matches the provided identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase order found"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token"),
            @ApiResponse(responseCode = "404", description = "Purchase order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderResponseDto> findById(
            @Parameter(description = "Purchase order unique identifier", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(purchaseBffService.findById(id));
    }

    /**
     * Creates a new purchase order in PENDING status.
     *
     * @param dto the purchase order data to create
     * @return the created purchase order with HTTP 201 status
     */
    @Operation(
            summary = "Create purchase order",
            description = "Creates a purchase order by forwarding the request to the purchase service.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Purchase order data to create",
                    required = true
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Purchase order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid purchase order request"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token"),
            @ApiResponse(responseCode = "404", description = "Supplier or product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<PurchaseOrderResponseDto> create(@Valid @RequestBody PurchaseOrderRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(purchaseBffService.create(dto));
    }

    /**
     * Confirms a PENDING purchase order.
     *
     * @param id the purchase order unique identifier
     * @return the confirmed purchase order
     */
    @Operation(
            summary = "Confirm purchase order",
            description = "Confirms a PENDING purchase order identified by the provided ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase order confirmed successfully"),
            @ApiResponse(responseCode = "400", description = "Purchase order is not in PENDING status"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token"),
            @ApiResponse(responseCode = "404", description = "Purchase order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/confirm")
    public ResponseEntity<PurchaseOrderResponseDto> confirm(
            @Parameter(description = "Purchase order unique identifier", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(purchaseBffService.confirm(id));
    }

    /**
     * Cancels a PENDING purchase order.
     *
     * @param id the purchase order unique identifier
     * @return the cancelled purchase order
     */
    @Operation(
            summary = "Cancel purchase order",
            description = "Cancels a PENDING purchase order identified by the provided ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Purchase order cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Purchase order is not in PENDING status"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token"),
            @ApiResponse(responseCode = "404", description = "Purchase order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/cancel")
    public ResponseEntity<PurchaseOrderResponseDto> cancel(
            @Parameter(description = "Purchase order unique identifier", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(purchaseBffService.cancel(id));
    }
}
