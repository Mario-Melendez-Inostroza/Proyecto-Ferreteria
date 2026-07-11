package com.ferreteria.bff.service;

import com.ferreteria.bff.client.PricingClient;
import com.ferreteria.bff.dto.PricingRequestDto;
import com.ferreteria.bff.dto.PricingResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PricingBffServiceTest {

    @Mock
    private PricingClient pricingClient;

    @Test
    void calculateShouldDelegateToClient() {
        PricingBffService service = new PricingBffService(pricingClient);
        UUID productId = UUID.randomUUID();
        PricingRequestDto request = new PricingRequestDto(productId, new BigDecimal("100.00"), 10, "promocion");
        PricingResponseDto response = new PricingResponseDto(productId, new BigDecimal("100.00"), 10,
                new BigDecimal("10.00"), new BigDecimal("90.00"), new BigDecimal("900.00"));
        when(pricingClient.calculate(request)).thenReturn(response);

        PricingResponseDto result = service.calculate(request);

        assertEquals(productId, result.productId());
    }
}
