package com.ferreteria.msreport.client;

import com.ferreteria.msreport.dto.InventoryDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

// Cliente HTTP para ms-inventory
@Component
public class InventoryClient {

    private final RestClient restClient;

    public InventoryClient(@Qualifier("inventoryRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    // Lista todo el inventario
    public List<InventoryDto> listarInventario() {
        return restClient.get()
                .uri("/inventory")
                .retrieve()
                .body(new ParameterizedTypeReference<List<InventoryDto>>() {});
    }

    // Lista los items de inventario con stock bajo
    public List<InventoryDto> listarStockBajo() {
        return restClient.get()
                .uri("/inventory/stock-bajo")
                .retrieve()
                .body(new ParameterizedTypeReference<List<InventoryDto>>() {});
    }
}
