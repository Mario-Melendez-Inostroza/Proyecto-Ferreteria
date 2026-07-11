package com.ferreteria.mspricing.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PricingRequestDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationForValidData() {
        PricingRequestDto dto = new PricingRequestDto(UUID.randomUUID(), BigDecimal.TEN, 5, "General");

        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldPassValidationWhenCategoryIsNull() {
        PricingRequestDto dto = new PricingRequestDto(UUID.randomUUID(), BigDecimal.TEN, 5, null);

        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFailValidationWhenRequiredFieldsAreMissingOrInvalid() {
        PricingRequestDto dto = new PricingRequestDto(null, null, 0, null);

        Set<ConstraintViolation<PricingRequestDto>> violations = validator.validate(dto);
        Set<String> fields = violations.stream()
                .map(v -> v.getPropertyPath().toString())
                .collect(Collectors.toSet());

        assertEquals(3, violations.size());
        assertTrue(fields.contains("productId"));
        assertTrue(fields.contains("unitPrice"));
        assertTrue(fields.contains("quantity"));
    }

    @Test
    void shouldFailValidationWhenUnitPriceIsNotPositive() {
        PricingRequestDto dto = new PricingRequestDto(UUID.randomUUID(), BigDecimal.ZERO, 5, "General");

        var violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("unitPrice", violations.iterator().next().getPropertyPath().toString());
    }
}
