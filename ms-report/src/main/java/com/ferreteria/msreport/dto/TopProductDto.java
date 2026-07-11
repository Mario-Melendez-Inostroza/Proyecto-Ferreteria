package com.ferreteria.msreport.dto;

import java.util.UUID;

// Respuesta del reporte de productos más vendidos
public record TopProductDto(
        UUID productId,
        String nombre,
        Long cantidadVendida
) {}
