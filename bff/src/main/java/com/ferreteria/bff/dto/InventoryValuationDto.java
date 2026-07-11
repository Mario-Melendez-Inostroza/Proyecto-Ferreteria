package com.ferreteria.bff.dto;

import java.math.BigDecimal;
import java.util.List;

public record InventoryValuationDto(
    List<InventoryValuationItemDto> items,
    BigDecimal valorTotalInventario) {
}
