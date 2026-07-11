package com.ferreteria.msreport.client;

import com.ferreteria.msreport.dto.ProductDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

// Cliente HTTP para ms-product
@Component
public class ProductClient {

    private final RestClient restClient;

    public ProductClient(@Qualifier("productRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    // Lista todos los productos
    public List<ProductDto> listarProductos() {
        return restClient.get()
                .uri("/products")
                .retrieve()
                .body(new ParameterizedTypeReference<List<ProductDto>>() {});
    }

    // Busca un producto por id
    public ProductDto buscarProducto(UUID id) {
        return restClient.get()
                .uri("/products/{id}", id)
                .retrieve()
                .body(ProductDto.class);
    }
}
