package com.ferreteria.mspricing.service;

import com.ferreteria.mspricing.config.DiscountProperties;
import com.ferreteria.mspricing.dto.PricingRequestDto;
import com.ferreteria.mspricing.dto.PricingResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Default implementation of {@link PricingService} that computes discounts
 * based on purchase volume and product category.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PricingServiceImpl implements PricingService {

    private static final String CATEGORIA_PROMOCION = "promocion";
    private static final BigDecimal CIEN = BigDecimal.valueOf(100);

    private final DiscountProperties discountProperties;

    /**
     * {@inheritDoc}
     * <p>
     * The final discount percentage is the MAXIMUM between the volume-based
     * discount and the category-based discount (they are not summed), to
     * avoid stacking discounts excessively.
     */
    @Override
    public PricingResponseDto calcular(PricingRequestDto dto) {
        BigDecimal descuentoVolumen = calcularDescuentoPorVolumen(dto.quantity());
        BigDecimal descuentoCategoria = calcularDescuentoPorCategoria(dto.category());

        BigDecimal descuentoPorcentaje = descuentoVolumen.max(descuentoCategoria);

        BigDecimal factor = BigDecimal.ONE.subtract(descuentoPorcentaje.divide(CIEN, 10, RoundingMode.HALF_UP));
        BigDecimal precioFinalUnitario = dto.unitPrice().multiply(factor).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalFinal = precioFinalUnitario.multiply(BigDecimal.valueOf(dto.quantity()))
                .setScale(2, RoundingMode.HALF_UP);

        if (descuentoPorcentaje.compareTo(BigDecimal.ZERO) > 0) {
            log.info("Descuento aplicado al producto {}: {}% (precio final unitario={}, total={})",
                    dto.productId(), descuentoPorcentaje, precioFinalUnitario, totalFinal);
        } else {
            log.info("Sin descuento aplicable para el producto {}", dto.productId());
        }

        return new PricingResponseDto(
                dto.productId(),
                dto.unitPrice(),
                dto.quantity(),
                descuentoPorcentaje,
                precioFinalUnitario,
                totalFinal
        );
    }

    private BigDecimal calcularDescuentoPorVolumen(Integer quantity) {
        Integer threshold = discountProperties.getVolume().getThreshold();
        if (threshold != null && quantity != null && quantity >= threshold) {
            return discountProperties.getVolume().getPercentage();
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal calcularDescuentoPorCategoria(String category) {
        if (category != null && CATEGORIA_PROMOCION.equalsIgnoreCase(category)) {
            return discountProperties.getCategory().getPromocion();
        }
        return BigDecimal.ZERO;
    }
}
