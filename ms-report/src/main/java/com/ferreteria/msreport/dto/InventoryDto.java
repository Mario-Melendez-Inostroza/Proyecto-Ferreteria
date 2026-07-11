package com.ferreteria.msreport.dto;

import java.util.UUID;

// DTO espejo de InventoryResponseDto de ms-inventory
public record InventoryDto(
        UUID id,
        UUID productId,
        Integer stock,
        Integer stockMinimo,
        boolean stockBajo
) {}
