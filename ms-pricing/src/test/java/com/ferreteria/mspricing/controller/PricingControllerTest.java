package com.ferreteria.mspricing.controller;

import com.ferreteria.mspricing.dto.PricingRequestDto;
import com.ferreteria.mspricing.dto.PricingResponseDto;
import com.ferreteria.mspricing.service.PricingService;
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
    private PricingService pricingService;

    @Test
    void calculateShouldReturnOk() {
        PricingController controller = new PricingController(pricingService);
        UUID productId = UUID.randomUUID();
        PricingRequestDto request = new PricingRequestDto(productId, BigDecimal.valueOf(100), 10, "promocion");
        PricingResponseDto response = new PricingResponseDto(productId, BigDecimal.valueOf(100), 10,
                BigDecimal.valueOf(10), new BigDecimal("90.00"), new BigDecimal("900.00"));
        when(pricingService.calcular(any())).thenReturn(response);

        ResponseEntity<PricingResponseDto> result = controller.calculate(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(productId, result.getBody().productId());
        assertEquals(new BigDecimal("900.00"), result.getBody().finalTotal());
    }
}
