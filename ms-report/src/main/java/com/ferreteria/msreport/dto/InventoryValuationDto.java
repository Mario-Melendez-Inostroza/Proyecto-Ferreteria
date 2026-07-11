package com.ferreteria.msreport.dto;

import java.math.BigDecimal;
import java.util.List;

// Respuesta del reporte de valorización de inventario
public record InventoryValuationDto(
        List<InventoryValuationItemDto> items,
        BigDecimal valorTotalInventario
) {}
