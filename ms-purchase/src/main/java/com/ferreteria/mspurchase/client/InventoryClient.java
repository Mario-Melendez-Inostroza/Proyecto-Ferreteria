package com.ferreteria.mspurchase.client;

import com.ferreteria.mspurchase.dto.AjustarStockDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

// Cliente HTTP para ms-inventory
@Component
public class InventoryClient {

    private final RestClient restClient;

    public InventoryClient(@Qualifier("inventoryRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    // Incrementa el stock del producto tras confirmar una orden de compra
    public void agregarStock(UUID productId, Integer cantidad) {
        restClient.put()
                .uri("/inventory/producto/{productId}/agregar", productId)
                .body(new AjustarStockDto(cantidad))
                .retrieve()
                .toBodilessEntity();
    }
}
