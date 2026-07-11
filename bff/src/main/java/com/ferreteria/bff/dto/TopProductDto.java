package com.ferreteria.bff.dto;

import java.util.UUID;

public record TopProductDto(
    UUID productId,
    String nombre,
    Long cantidadVendida) {
}
