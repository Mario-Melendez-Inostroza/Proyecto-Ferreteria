package com.ferreteria.bff.service;

import com.ferreteria.bff.client.ReportClient;
import com.ferreteria.bff.dto.InventoryValuationDto;
import com.ferreteria.bff.dto.LowStockAlertDto;
import com.ferreteria.bff.dto.SalesSummaryReportDto;
import com.ferreteria.bff.dto.TopProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * BFF service for reporting operations. Contains no business logic of its
 * own; it simply delegates each call to {@link ReportClient}, the HTTP
 * client for the report microservice.
 */
@Service
@RequiredArgsConstructor
public class ReportBffService {

    private final ReportClient reportClient;

    /**
     * Delegates the retrieval of the sales summary report to the report
     * microservice.
     *
     * @return the sales summary
     */
    public SalesSummaryReportDto salesSummary() {
        return reportClient.salesSummary();
    }

    /**
     * Delegates the retrieval of the top-selling products report to the
     * report microservice.
     *
     * @param limit maximum number of products to include
     * @return the list of top products
     */
    public List<TopProductDto> topProducts(int limit) {
        return reportClient.topProducts(limit);
    }

    /**
     * Delegates the retrieval of the inventory valuation report to the
     * report microservice.
     *
     * @return the inventory valuation report
     */
    public InventoryValuationDto inventoryValuation() {
        return reportClient.inventoryValuation();
    }

    /**
     * Delegates the retrieval of the low stock alerts report to the report
     * microservice.
     *
     * @return the list of low stock alerts
     */
    public List<LowStockAlertDto> lowStockAlerts() {
        return reportClient.lowStockAlerts();
    }
}
