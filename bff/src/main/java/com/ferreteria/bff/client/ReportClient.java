package com.ferreteria.bff.client;

import com.ferreteria.bff.dto.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.List;

@Component
public class ReportClient {

    private final RestClient restClient;

    public ReportClient(@Qualifier("reportRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public SalesSummaryReportDto salesSummary() {
        return restClient.get().uri("/reports/sales-summary").retrieve()
                .body(SalesSummaryReportDto.class);
    }

    public List<TopProductDto> topProducts(int limit) {
        return restClient.get().uri("/reports/top-products?limit={limit}", limit).retrieve()
                .body(new ParameterizedTypeReference<List<TopProductDto>>() {});
    }

    public InventoryValuationDto inventoryValuation() {
        return restClient.get().uri("/reports/inventory-valuation").retrieve()
                .body(InventoryValuationDto.class);
    }

    public List<LowStockAlertDto> lowStockAlerts() {
        return restClient.get().uri("/reports/low-stock-alerts").retrieve()
                .body(new ParameterizedTypeReference<List<LowStockAlertDto>>() {});
    }
}
