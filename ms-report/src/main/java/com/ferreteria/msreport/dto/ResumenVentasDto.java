package com.ferreteria.msreport.dto;

import java.math.BigDecimal;

// DTO espejo del resumen de ventas de ms-sales
public record ResumenVentasDto(
        Long totalVentas,
        BigDecimal montoTotal
) {}
