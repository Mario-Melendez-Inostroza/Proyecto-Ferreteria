package com.ferreteria.bff.service;

import com.ferreteria.bff.client.ReportClient;
import com.ferreteria.bff.dto.InventoryValuationDto;
import com.ferreteria.bff.dto.LowStockAlertDto;
import com.ferreteria.bff.dto.SalesSummaryReportDto;
import com.ferreteria.bff.dto.TopProductDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportBffServiceTest {

    @Mock
    private ReportClient reportClient;

    @Test
    void salesSummaryShouldDelegateToClient() {
        ReportBffService service = new ReportBffService(reportClient);
        when(reportClient.salesSummary()).thenReturn(new SalesSummaryReportDto(10L, new BigDecimal("1000.00")));

        SalesSummaryReportDto result = service.salesSummary();

        assertEquals(10L, result.totalVentas());
    }

    @Test
    void topProductsShouldDelegateToClient() {
        ReportBffService service = new ReportBffService(reportClient);
        when(reportClient.topProducts(5)).thenReturn(List.of(new TopProductDto(UUID.randomUUID(), "Taladro", 5L)));

        List<TopProductDto> result = service.topProducts(5);

        assertEquals(1, result.size());
    }

    @Test
    void inventoryValuationShouldDelegateToClient() {
        ReportBffService service = new ReportBffService(reportClient);
        when(reportClient.inventoryValuation()).thenReturn(new InventoryValuationDto(List.of(), new BigDecimal("500.00")));

        InventoryValuationDto result = service.inventoryValuation();

        assertEquals(new BigDecimal("500.00"), result.valorTotalInventario());
    }

    @Test
    void lowStockAlertsShouldDelegateToClient() {
        ReportBffService service = new ReportBffService(reportClient);
        when(reportClient.lowStockAlerts()).thenReturn(List.of(new LowStockAlertDto(UUID.randomUUID(), "Taladro", 2, 5)));

        List<LowStockAlertDto> result = service.lowStockAlerts();

        assertEquals(1, result.size());
    }
}
