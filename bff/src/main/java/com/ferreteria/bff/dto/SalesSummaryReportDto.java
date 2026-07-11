package com.ferreteria.bff.dto;

import java.math.BigDecimal;

public record SalesSummaryReportDto(
    Long totalVentas,
    BigDecimal montoTotal) {
}
