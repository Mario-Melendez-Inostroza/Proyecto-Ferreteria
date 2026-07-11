package com.ferreteria.mspurchase.dto;

import java.util.UUID;

// DTO espejo de ms-product, solo con los campos necesarios para ms-purchase
public record ProductDto(UUID id, String nombre, Boolean activo) {
}
