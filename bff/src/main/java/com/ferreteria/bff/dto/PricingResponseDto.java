package com.ferreteria.bff.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PricingResponseDto(
    UUID productId,
    BigDecimal unitPrice,
    Integer quantity,
    BigDecimal discountPercentage,
    BigDecimal finalUnitPrice,
    BigDecimal finalTotal) {
}
