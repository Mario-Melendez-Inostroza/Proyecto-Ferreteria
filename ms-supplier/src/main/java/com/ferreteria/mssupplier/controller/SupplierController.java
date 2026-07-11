package com.ferreteria.mssupplier.controller;

import com.ferreteria.mssupplier.dto.*;
import com.ferreteria.mssupplier.service.SupplierService;
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
 * REST controller exposing operations to browse, create, update, and
 * deactivate suppliers of the hardware store.
 */
@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
@Tag(name = "Suppliers", description = "Endpoints for supplier management")
public class SupplierController {

    private final SupplierService supplierService;

    /**
     * Lists all active suppliers.
     *
     * @return the list of active suppliers
     */
    @Operation(
            summary = "List active suppliers",
            description = "Returns all active suppliers."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suppliers retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<SupplierResponseDto>> listar() {
        return ResponseEntity.ok(supplierService.listarActivos());
    }

    /**
     * Finds a supplier by its unique identifier.
     *
     * @param id the supplier unique identifier
     * @return the matching supplier
     */
    @Operation(
            summary = "Find supplier by ID",
            description = "Returns supplier information using its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier found"),
            @ApiResponse(responseCode = "400", description = "Invalid supplier identifier"),
            @ApiResponse(responseCode = "404", description = "Supplier not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> buscarPorId(
            @Parameter(description = "Supplier unique identifier", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(supplierService.buscarPorId(id));
    }

    /**
     * Searches active suppliers whose names contain the given value.
     *
     * @param nombre the supplier name search value
     * @return the list of matching active suppliers
     */
    @Operation(
            summary = "Search suppliers by name",
            description = "Returns active suppliers whose names contain the provided value."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Suppliers retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search parameter"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<SupplierResponseDto>> buscarPorNombre(
            @Parameter(description = "Supplier name search value", required = true)
            @RequestParam String nombre) {
        return ResponseEntity.ok(supplierService.buscarPorNombre(nombre));
    }

    /**
     * Creates a new supplier.
     *
     * @param dto the supplier data to create
     * @return the newly created supplier
     */
    @Operation(
            summary = "Create supplier",
            description = "Registers a new supplier.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Supplier data to create",
                    required = true
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Supplier created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid supplier request"),
            @ApiResponse(responseCode = "409", description = "Supplier already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<SupplierResponseDto> crear(@Valid @RequestBody SupplierRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.crear(dto));
    }

    /**
     * Updates an existing supplier identified by its unique identifier.
     *
     * @param id  the supplier unique identifier
     * @param dto the updated supplier data
     * @return the updated supplier
     */
    @Operation(
            summary = "Update supplier",
            description = "Updates an existing supplier by its unique identifier.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated supplier data",
                    required = true
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid supplier request"),
            @ApiResponse(responseCode = "404", description = "Supplier not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponseDto> actualizar(
            @Parameter(description = "Supplier unique identifier", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody SupplierRequestDto dto) {
        return ResponseEntity.ok(supplierService.actualizar(id, dto));
    }

    /**
     * Deactivates an existing supplier by its unique identifier. This is a
     * soft delete: the record is not physically removed.
     *
     * @param id the supplier unique identifier
     * @return a confirmation message
     */
    @Operation(
            summary = "Deactivate supplier",
            description = "Marks an existing supplier as inactive by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier deactivated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid supplier identifier"),
            @ApiResponse(responseCode = "404", description = "Supplier not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDto> desactivar(
            @Parameter(description = "Supplier unique identifier", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(supplierService.desactivar(id));
    }
}
