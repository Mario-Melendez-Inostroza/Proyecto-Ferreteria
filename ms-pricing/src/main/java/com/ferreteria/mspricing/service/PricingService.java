package com.ferreteria.mspricing.service;

import com.ferreteria.mspricing.dto.PricingRequestDto;
import com.ferreteria.mspricing.dto.PricingResponseDto;

/**
 * Service that calculates discounts and final prices for a product given a
 * unit price and quantity.
 */
public interface PricingService {

    /**
     * Calculates the applicable discount and final pricing for the given request.
     *
     * @param dto the pricing request data
     * @return the calculated pricing result
     */
    PricingResponseDto calcular(PricingRequestDto dto);
}
