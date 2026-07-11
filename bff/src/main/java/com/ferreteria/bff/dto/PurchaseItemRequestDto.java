package com.ferreteria.bff.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record PurchaseItemRequestDto(
    @NotNull(message = "Product id is required") UUID productId,
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1") Integer quantity,
    @NotNull(message = "Unit cost is required")
    @Positive(message = "Unit cost must be positive") BigDecimal unitCost) {
}
