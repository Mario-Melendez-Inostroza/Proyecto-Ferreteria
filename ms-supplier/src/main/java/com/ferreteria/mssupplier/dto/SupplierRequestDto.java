package com.ferreteria.mssupplier.dto;

import jakarta.validation.constraints.NotBlank;

public record SupplierRequestDto(
        @NotBlank(message = "Nombre es obligatorio") String name,
        @NotBlank(message = "RUT es obligatorio") String rut,
        String contactName,
        String phone,
        String email,
        String address
) {
}
