package com.ferreteria.mspurchase.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PurchaseItemResponseDto(
        UUID productId,
        Integer quantity,
        BigDecimal unitCost) {
}
