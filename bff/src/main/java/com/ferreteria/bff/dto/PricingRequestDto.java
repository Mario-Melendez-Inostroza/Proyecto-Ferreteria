package com.ferreteria.bff.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record PricingRequestDto(
    @NotNull(message = "Product id is required") UUID productId,
    @NotNull(message = "Unit price is required")
    @Positive(message = "Unit price must be greater than 0") BigDecimal unitPrice,
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1") Integer quantity,
    String category) {
}
