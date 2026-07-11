package com.ferreteria.mssupplier.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SupplierRequestDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationForValidData() {
        SupplierRequestDto dto = new SupplierRequestDto("Ferretería El Tornillo", "76.111.222-3", "Juan Pérez",
                "+56 9 1111 2222", "contacto@eltornillo.cl", "Av. Industrial 450");

        assertTrue(validator.validate(dto).isEmpty());
    }

    @Test
    void shouldFailValidationWhenNameAndRutAreBlank() {
        SupplierRequestDto dto = new SupplierRequestDto("", "", "Juan Pérez", "+56 9 1111 2222",
                "contacto@eltornillo.cl", "Av. Industrial 450");

        var violations = validator.validate(dto);
        Set<String> fields = violations.stream().map(v -> v.getPropertyPath().toString()).collect(Collectors.toSet());

        assertEquals(2, violations.size());
        assertTrue(fields.contains("name"));
        assertTrue(fields.contains("rut"));
    }
}
