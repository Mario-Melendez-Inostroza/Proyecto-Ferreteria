package com.ferreteria.msreport.dto;

import java.math.BigDecimal;

// Respuesta del reporte de resumen de ventas
public record SalesSummaryDto(
        Long totalVentas,
        BigDecimal montoTotal
) {}
