package com.ferreteria.msreport.dto;

import java.util.UUID;

// Respuesta del reporte de alertas de stock bajo
public record LowStockAlertDto(
        UUID productId,
        String nombre,
        Integer stock,
        Integer stockMinimo
) {}
