package com.ferreteria.mspricing.service;

import com.ferreteria.mspricing.config.DiscountProperties;
import com.ferreteria.mspricing.dto.PricingRequestDto;
import com.ferreteria.mspricing.dto.PricingResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PricingServiceImplTest {

    private PricingServiceImpl pricingService;

    @BeforeEach
    void setUp() {
        DiscountProperties discountProperties = new DiscountProperties();
        discountProperties.getVolume().setThreshold(10);
        discountProperties.getVolume().setPercentage(BigDecimal.valueOf(5));
        discountProperties.getCategory().setPromocion(BigDecimal.valueOf(10));

        pricingService = new PricingServiceImpl(discountProperties);
    }

    @Test
    void calcularShouldNotApplyDiscountWhenQuantityIsLowAndNoCategory() {
        PricingRequestDto dto = new PricingRequestDto(UUID.randomUUID(), BigDecimal.valueOf(100), 3, null);

        PricingResponseDto result = pricingService.calcular(dto);

        assertEquals(BigDecimal.ZERO, result.discountPercentage());
        assertEquals(new BigDecimal("100.00"), result.finalUnitPrice());
        assertEquals(new BigDecimal("300.00"), result.finalTotal());
    }

    @Test
    void calcularShouldApplyVolumeDiscountWhenQuantityReachesThreshold() {
        PricingRequestDto dto = new PricingRequestDto(UUID.randomUUID(), BigDecimal.valueOf(100), 10, null);

        PricingResponseDto result = pricingService.calcular(dto);

        assertEquals(BigDecimal.valueOf(5), result.discountPercentage());
        assertEquals(new BigDecimal("95.00"), result.finalUnitPrice());
        assertEquals(new BigDecimal("950.00"), result.finalTotal());
    }

    @Test
    void calcularShouldApplyCategoryDiscountWhenCategoryIsPromocion() {
        PricingRequestDto dto = new PricingRequestDto(UUID.randomUUID(), BigDecimal.valueOf(100), 2, "promocion");

        PricingResponseDto result = pricingService.calcular(dto);

        assertEquals(BigDecimal.valueOf(10), result.discountPercentage());
        assertEquals(new BigDecimal("90.00"), result.finalUnitPrice());
        assertEquals(new BigDecimal("180.00"), result.finalTotal());
    }

    @Test
    void calcularShouldApplyMaximumDiscountWhenBothAreApplicable() {
        PricingRequestDto dto = new PricingRequestDto(UUID.randomUUID(), BigDecimal.valueOf(100), 10, "promocion");

        PricingResponseDto result = pricingService.calcular(dto);

        // Volumen aplica 5%, categoria aplica 10%: debe tomar el maximo (10%), no la suma (15%).
        assertEquals(BigDecimal.valueOf(10), result.discountPercentage());
        assertEquals(new BigDecimal("90.00"), result.finalUnitPrice());
        assertEquals(new BigDecimal("900.00"), result.finalTotal());
    }

    @Test
    void calcularShouldBeCaseInsensitiveForCategory() {
        PricingRequestDto dto = new PricingRequestDto(UUID.randomUUID(), BigDecimal.valueOf(100), 1, "PROMOCION");

        PricingResponseDto result = pricingService.calcular(dto);

        assertEquals(BigDecimal.valueOf(10), result.discountPercentage());
    }
}
