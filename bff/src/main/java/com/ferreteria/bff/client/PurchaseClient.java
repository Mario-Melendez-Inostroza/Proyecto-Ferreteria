package com.ferreteria.bff.client;

import com.ferreteria.bff.dto.PurchaseOrderRequestDto;
import com.ferreteria.bff.dto.PurchaseOrderResponseDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.UUID;

@Component
public class PurchaseClient {

    private final RestClient restClient;

    public PurchaseClient(@Qualifier("purchaseRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public List<PurchaseOrderResponseDto> findAll() {
        return restClient.get().uri("/purchases").retrieve()
                .body(new ParameterizedTypeReference<List<PurchaseOrderResponseDto>>() {});
    }

    public PurchaseOrderResponseDto findById(UUID id) {
        return restClient.get().uri("/purchases/{id}", id).retrieve()
                .body(PurchaseOrderResponseDto.class);
    }

    public PurchaseOrderResponseDto create(PurchaseOrderRequestDto dto) {
        return restClient.post().uri("/purchases")
                .contentType(MediaType.APPLICATION_JSON).body(dto)
                .retrieve().body(PurchaseOrderResponseDto.class);
    }

    public PurchaseOrderResponseDto confirm(UUID id) {
        return restClient.post().uri("/purchases/{id}/confirm", id)
                .retrieve().body(PurchaseOrderResponseDto.class);
    }

    public PurchaseOrderResponseDto cancel(UUID id) {
        return restClient.post().uri("/purchases/{id}/cancel", id)
                .retrieve().body(PurchaseOrderResponseDto.class);
    }
}
