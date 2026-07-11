package com.ferreteria.mspurchase.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record PurchaseOrderRequestDto(
        @NotNull(message = "El proveedor es obligatorio")
        UUID supplierId,

        @NotEmpty(message = "La orden debe tener al menos un ítem")
        @Valid
        List<PurchaseItemRequestDto> items) {
}
