package com.ferreteria.mspurchase.dto;

import java.util.UUID;

// DTO espejo de ms-supplier, solo con los campos necesarios para ms-purchase
public record SupplierDto(UUID id, String name, Boolean active) {
}
