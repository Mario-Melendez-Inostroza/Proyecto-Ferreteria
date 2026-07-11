package com.ferreteria.mspricing.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request payload for a pricing/discount calculation.
 *
 * @param productId the product unique identifier
 * @param unitPrice the unit price of the product
 * @param quantity the quantity being purchased
 * @param category the product category, used for category-based discounts (optional)
 */
public record PricingRequestDto(

        @NotNull(message = "El identificador del producto es obligatorio")
        UUID productId,

        @NotNull(message = "El precio unitario es obligatorio")
        @Positive(message = "El precio unitario debe ser mayor que cero")
        BigDecimal unitPrice,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        Integer quantity,

        String category
) {
}
