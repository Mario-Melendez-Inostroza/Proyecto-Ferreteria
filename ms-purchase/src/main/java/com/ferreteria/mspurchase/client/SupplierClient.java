package com.ferreteria.mspurchase.client;

import com.ferreteria.mspurchase.dto.SupplierDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

// Cliente HTTP para ms-supplier
@Component
public class SupplierClient {

    private final RestClient restClient;

    public SupplierClient(@Qualifier("supplierRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    // Obtiene info del proveedor
    public SupplierDto buscarProveedor(UUID supplierId) {
        return restClient.get()
                .uri("/suppliers/{id}", supplierId)
                .retrieve()
                .body(SupplierDto.class);
    }
}
