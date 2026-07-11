package com.ferreteria.msreport.client;

import com.ferreteria.msreport.dto.ResumenVentasDto;
import com.ferreteria.msreport.dto.VentaDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

// Cliente HTTP para ms-sales
@Component
public class SalesClient {

    private final RestClient restClient;

    public SalesClient(@Qualifier("salesRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    // Obtiene el resumen agregado de ventas
    public ResumenVentasDto obtenerResumen() {
        return restClient.get()
                .uri("/sales/resumen")
                .retrieve()
                .body(ResumenVentasDto.class);
    }

    // Lista todas las ventas registradas
    public List<VentaDto> listarVentas() {
        return restClient.get()
                .uri("/sales")
                .retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<List<VentaDto>>() {});
    }
}
