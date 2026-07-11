package com.ferreteria.msreport.dto;

import java.math.BigDecimal;
import java.util.UUID;

// Item del reporte de valorización de inventario
public record InventoryValuationItemDto(
        UUID productId,
        String nombre,
        Integer stock,
        BigDecimal precioUnitario,
        BigDecimal valorTotal
) {}
