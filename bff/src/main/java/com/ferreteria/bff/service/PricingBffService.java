package com.ferreteria.bff.service;

import com.ferreteria.bff.client.PricingClient;
import com.ferreteria.bff.dto.PricingRequestDto;
import com.ferreteria.bff.dto.PricingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * BFF service for pricing operations. Contains no business logic of its
 * own; it simply delegates each call to {@link PricingClient}, the HTTP
 * client for the pricing microservice.
 */
@Service
@RequiredArgsConstructor
public class PricingBffService {

    private final PricingClient pricingClient;

    /**
     * Delegates the calculation of a discounted price to the pricing
     * microservice.
     *
     * @param request the pricing calculation input
     * @return the calculated pricing result
     */
    public PricingResponseDto calculate(PricingRequestDto request) {
        return pricingClient.calculate(request);
    }
}
