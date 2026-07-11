package com.ferreteria.mssupplier.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SupplierResponseDto(
        UUID id,
        String name,
        String rut,
        String contactName,
        String phone,
        String email,
        String address,
        Boolean active,
        LocalDateTime createdAt
) {
}
