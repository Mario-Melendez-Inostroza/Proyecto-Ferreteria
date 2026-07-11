package com.ferreteria.msreport.dto;

import java.math.BigDecimal;
import java.util.UUID;

// DTO espejo del producto de ms-product
public record ProductDto(
        UUID id,
        String nombre,
        BigDecimal precio,
        String categoria,
        Boolean activo
) {}
