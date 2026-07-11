package com.ferreteria.bff.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record InventoryValuationItemDto(
    UUID productId,
    String nombre,
    Integer stock,
    BigDecimal precioUnitario,
    BigDecimal valorTotal) {
}
