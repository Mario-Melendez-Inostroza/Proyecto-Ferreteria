package com.ferreteria.bff.client;

import com.ferreteria.bff.dto.PricingRequestDto;
import com.ferreteria.bff.dto.PricingResponseDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PricingClient {

    private final RestClient restClient;

    public PricingClient(@Qualifier("pricingRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public PricingResponseDto calculate(PricingRequestDto dto) {
        return restClient.post().uri("/pricing/calculate")
                .contentType(MediaType.APPLICATION_JSON).body(dto)
                .retrieve().body(PricingResponseDto.class);
    }
}
