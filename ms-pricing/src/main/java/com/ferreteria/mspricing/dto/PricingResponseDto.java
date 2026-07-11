package com.ferreteria.mspricing.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Response payload with the result of a pricing/discount calculation.
 *
 * @param productId the product unique identifier
 * @param unitPrice the original unit price of the product
 * @param quantity the quantity purchased
 * @param discountPercentage the discount percentage applied
 * @param finalUnitPrice the unit price after applying the discount
 * @param finalTotal the total price (finalUnitPrice * quantity)
 */
public record PricingResponseDto(
        UUID productId,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal discountPercentage,
        BigDecimal finalUnitPrice,
        BigDecimal finalTotal
) {
}
