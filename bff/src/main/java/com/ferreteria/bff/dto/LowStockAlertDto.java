package com.ferreteria.bff.dto;

import java.util.UUID;

public record LowStockAlertDto(
    UUID productId,
    String nombre,
    Integer stock,
    Integer stockMinimo) {
}
