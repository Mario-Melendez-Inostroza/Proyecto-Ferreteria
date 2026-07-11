package com.ferreteria.bff.controller;

import com.ferreteria.bff.dto.*;
import com.ferreteria.bff.service.SupplierBffService;
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
 * BFF controller exposing supplier management endpoints to the frontend,
 * delegating all operations to the supplier microservice through
 * {@link SupplierBffService}.
 */
@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
@Tag(name = "Suppliers", description = "Endpoints for supplier management")
public class SupplierController {

    private final SupplierBffService supplierBffService;

    /**
     * Returns all active suppliers available through the supplier service.
     *
     * @return the list of active suppliers
     */
    @Operation(
            summary = "List suppliers",
            description = "Returns all active suppliers available through the supplier service."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suppliers retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<SupplierResponseDto>> findAll() {
        return ResponseEntity.ok(supplierBffService.findAll());
    }

    /**
     * Returns a supplier that matches the provided supplier identifier.
     *
     * @param id the supplier unique identifier
     * @return the matching supplier
     */
    @Operation(
            summary = "Find supplier by ID",
            description = "Returns a supplier that matches the provided supplier identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier found"),
            @ApiResponse(responseCode = "400", description = "Invalid supplier identifier"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token"),
            @ApiResponse(responseCode = "404", description = "Supplier not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> findById(
            @Parameter(description = "Supplier unique identifier", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(supplierBffService.findById(id));
    }

    /**
     * Returns suppliers whose names match the provided search value.
     *
     * @param name the supplier name search value
     * @return the list of matching suppliers
     */
    @Operation(
            summary = "Search suppliers by name",
            description = "Returns suppliers whose names match the provided search value."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suppliers retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search parameter"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<SupplierResponseDto>> findByName(
            @Parameter(description = "Supplier name search value", required = true)
            @RequestParam("nombre") String name) {
        return ResponseEntity.ok(supplierBffService.findByName(name));
    }

    /**
     * Creates a new supplier.
     *
     * @param dto the supplier data to create
     * @return the created supplier with HTTP 201 status
     */
    @Operation(
            summary = "Create supplier",
            description = "Creates a new supplier by forwarding the request to the supplier service.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Supplier data to create",
                    required = true
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Supplier created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid supplier request"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<SupplierResponseDto> create(@Valid @RequestBody SupplierRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(supplierBffService.create(dto));
    }

    /**
     * Updates an existing supplier identified by the provided supplier ID.
     *
     * @param id the supplier unique identifier
     * @param dto the updated supplier data
     * @return the updated supplier
     */
    @Operation(
            summary = "Update supplier",
            description = "Updates an existing supplier identified by the provided supplier ID.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated supplier data",
                    required = true
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid supplier request"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token"),
            @ApiResponse(responseCode = "404", description = "Supplier not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> update(
            @Parameter(description = "Supplier unique identifier", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody SupplierRequestDto dto) {
        return ResponseEntity.ok(supplierBffService.update(id, dto));
    }

    /**
     * Deactivates a supplier identified by the provided supplier ID.
     *
     * @param id the supplier unique identifier
     * @return a confirmation message
     */
    @Operation(
            summary = "Deactivate supplier",
            description = "Deactivates a supplier identified by the provided supplier ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier deactivated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid supplier identifier"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT token"),
            @ApiResponse(responseCode = "404", description = "Supplier not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDto> deactivate(
            @Parameter(description = "Supplier unique identifier", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(supplierBffService.deactivate(id));
    }
}
