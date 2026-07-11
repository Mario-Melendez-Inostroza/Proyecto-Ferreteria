package com.ferreteria.msreport.dto;

import java.math.BigDecimal;
import java.util.UUID;

// DTO espejo de VentaResponseDto de ms-sales
public record VentaDto(
        UUID id,
        UUID productId,
        String nombreProducto,
        Integer cantidad,
        BigDecimal total
) {}
