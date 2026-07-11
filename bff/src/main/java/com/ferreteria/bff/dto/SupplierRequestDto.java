package com.ferreteria.bff.dto;

import jakarta.validation.constraints.NotBlank;

public record SupplierRequestDto(
    @NotBlank(message = "Name is required") String name,
    @NotBlank(message = "RUT is required") String rut,
    String contactName,
    String phone,
    String email,
    String address) {
}
