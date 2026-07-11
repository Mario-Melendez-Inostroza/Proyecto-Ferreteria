package com.ferreteria.bff.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PurchaseOrderResponseDto(
    UUID id,
    UUID supplierId,
    String status,
    List<PurchaseItemResponseDto> items,
    BigDecimal totalCost,
    LocalDateTime createdAt,
    LocalDateTime confirmedAt) {
}
