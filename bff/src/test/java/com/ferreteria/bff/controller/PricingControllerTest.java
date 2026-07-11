package com.ferreteria.bff.controller;

import com.ferreteria.bff.dto.PricingRequestDto;
import com.ferreteria.bff.dto.PricingResponseDto;
import com.ferreteria.bff.service.PricingBffService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PricingControllerTest {

    @Mock
    private PricingBffService pricingBffService;

    @Test
    void calculateShouldReturnOk() {
        PricingController controller = new PricingController(pricingBffService);
        UUID productId = UUID.randomUUID();
        PricingRequestDto request = new PricingRequestDto(productId, new BigDecimal("100.00"), 10, "promocion");
        PricingResponseDto response = new PricingResponseDto(productId, new BigDecimal("100.00"), 10,
                new BigDecimal("10.00"), new BigDecimal("90.00"), new BigDecimal("900.00"));
        when(pricingBffService.calculate(any())).thenReturn(response);

        ResponseEntity<PricingResponseDto> result = controller.calculate(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(productId, result.getBody().productId());
    }
}
