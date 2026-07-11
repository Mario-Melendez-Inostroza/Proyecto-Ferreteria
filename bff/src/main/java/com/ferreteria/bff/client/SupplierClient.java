package com.ferreteria.bff.client;

import com.ferreteria.bff.dto.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.UUID;

@Component
public class SupplierClient {

    private final RestClient restClient;

    public SupplierClient(@Qualifier("supplierRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public List<SupplierResponseDto> findAll() {
        return restClient.get().uri("/suppliers").retrieve()
                .body(new ParameterizedTypeReference<List<SupplierResponseDto>>() {});
    }

    public SupplierResponseDto findById(UUID id) {
        return restClient.get().uri("/suppliers/{id}", id).retrieve()
                .body(SupplierResponseDto.class);
    }

    public List<SupplierResponseDto> findByName(String name) {
        return restClient.get().uri("/suppliers/buscar?nombre={name}", name).retrieve()
                .body(new ParameterizedTypeReference<List<SupplierResponseDto>>() {});
    }

    public SupplierResponseDto create(SupplierRequestDto dto) {
        return restClient.post().uri("/suppliers")
                .contentType(MediaType.APPLICATION_JSON).body(dto)
                .retrieve().body(SupplierResponseDto.class);
    }

    public SupplierResponseDto update(UUID id, SupplierRequestDto dto) {
        return restClient.put().uri("/suppliers/{id}", id)
                .contentType(MediaType.APPLICATION_JSON).body(dto)
                .retrieve().body(SupplierResponseDto.class);
    }

    public MessageResponseDto deactivate(UUID id) {
        return restClient.delete().uri("/suppliers/{id}", id)
                .retrieve().body(MessageResponseDto.class);
    }
}
