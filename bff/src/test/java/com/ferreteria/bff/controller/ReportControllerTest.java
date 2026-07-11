package com.ferreteria.bff.controller;

import com.ferreteria.bff.dto.InventoryValuationDto;
import com.ferreteria.bff.dto.LowStockAlertDto;
import com.ferreteria.bff.dto.SalesSummaryReportDto;
import com.ferreteria.bff.dto.TopProductDto;
import com.ferreteria.bff.service.ReportBffService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @Mock
    private ReportBffService reportBffService;

    @Test
    void salesSummaryShouldReturnOk() {
        ReportController controller = new ReportController(reportBffService);
        SalesSummaryReportDto response = new SalesSummaryReportDto(10L, new BigDecimal("1000.00"));
        when(reportBffService.salesSummary()).thenReturn(response);

        ResponseEntity<SalesSummaryReportDto> result = controller.salesSummary();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(10L, result.getBody().totalVentas());
    }

    @Test
    void topProductsShouldReturnOk() {
        ReportController controller = new ReportController(reportBffService);
        TopProductDto response = new TopProductDto(UUID.randomUUID(), "Taladro", 5L);
        when(reportBffService.topProducts(5)).thenReturn(List.of(response));

        ResponseEntity<List<TopProductDto>> result = controller.topProducts(5);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void inventoryValuationShouldReturnOk() {
        ReportController controller = new ReportController(reportBffService);
        InventoryValuationDto response = new InventoryValuationDto(List.of(), new BigDecimal("500.00"));
        when(reportBffService.inventoryValuation()).thenReturn(response);

        ResponseEntity<InventoryValuationDto> result = controller.inventoryValuation();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void lowStockAlertsShouldReturnOk() {
        ReportController controller = new ReportController(reportBffService);
        LowStockAlertDto response = new LowStockAlertDto(UUID.randomUUID(), "Taladro", 2, 5);
        when(reportBffService.lowStockAlerts()).thenReturn(List.of(response));

        ResponseEntity<List<LowStockAlertDto>> result = controller.lowStockAlerts();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
    }
}
