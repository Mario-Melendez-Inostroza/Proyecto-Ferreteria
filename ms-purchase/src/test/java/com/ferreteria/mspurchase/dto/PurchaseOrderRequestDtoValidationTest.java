package com.ferreteria.mspurchase.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PurchaseOrderRequestDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationForValidData() {
        PurchaseOrderRequestDto dto = new PurchaseOrderRequestDto(UUID.randomUUID(),
                List.of(new PurchaseItemRequestDto(UUID.randomUUID(), 5, new BigDecimal("10.00"))));

        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFailValidationWhenSupplierAndItemsAreMissing() {
        PurchaseOrderRequestDto dto = new PurchaseOrderRequestDto(null, List.of());

        var violations = validator.validate(dto);
        Set<String> fields = violations.stream().map(v -> v.getPropertyPath().toString()).collect(Collectors.toSet());

        assertEquals(2, violations.size());
        assertTrue(fields.contains("supplierId"));
        assertTrue(fields.contains("items"));
    }

    @Test
    void shouldFailValidationWhenItemFieldsAreInvalid() {
        PurchaseOrderRequestDto dto = new PurchaseOrderRequestDto(UUID.randomUUID(),
                List.of(new PurchaseItemRequestDto(null, 0, new BigDecimal("-1"))));

        var violations = validator.validate(dto);

        assertEquals(3, violations.size());
    }
}
