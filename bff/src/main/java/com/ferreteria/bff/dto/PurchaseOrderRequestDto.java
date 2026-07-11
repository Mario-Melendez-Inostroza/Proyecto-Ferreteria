package com.ferreteria.bff.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record PurchaseOrderRequestDto(
    @NotNull(message = "Supplier id is required") UUID supplierId,
    @NotEmpty(message = "The order must have at least one item")
    @Valid List<PurchaseItemRequestDto> items) {
}
