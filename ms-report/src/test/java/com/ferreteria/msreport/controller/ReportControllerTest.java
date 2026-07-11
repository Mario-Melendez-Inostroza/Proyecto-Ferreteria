package com.ferreteria.msreport.controller;

import com.ferreteria.msreport.dto.InventoryValuationDto;
import com.ferreteria.msreport.dto.InventoryValuationItemDto;
import com.ferreteria.msreport.dto.LowStockAlertDto;
import com.ferreteria.msreport.dto.SalesSummaryDto;
import com.ferreteria.msreport.dto.TopProductDto;
import com.ferreteria.msreport.service.ReportService;
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
    private ReportService reportService;

    @Test
    void salesSummaryShouldReturnOk() {
        ReportController controller = new ReportController(reportService);
        SalesSummaryDto response = new SalesSummaryDto(10L, new BigDecimal("100000"));
        when(reportService.obtenerResumenVentas()).thenReturn(response);

        ResponseEntity<SalesSummaryDto> result = controller.salesSummary();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(10L, result.getBody().totalVentas());
    }

    @Test
    void topProductsShouldReturnOk() {
        ReportController controller = new ReportController(reportService);
        UUID productId = UUID.randomUUID();
        TopProductDto product = new TopProductDto(productId, "Taladro", 15L);
        when(reportService.obtenerTopProductos(5)).thenReturn(List.of(product));

        ResponseEntity<List<TopProductDto>> result = controller.topProducts(5);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals("Taladro", result.getBody().get(0).nombre());
    }

    @Test
    void inventoryValuationShouldReturnOk() {
        ReportController controller = new ReportController(reportService);
        UUID productId = UUID.randomUUID();
        InventoryValuationItemDto item = new InventoryValuationItemDto(productId, "Taladro", 10,
                new BigDecimal("1000"), new BigDecimal("10000"));
        InventoryValuationDto response = new InventoryValuationDto(List.of(item), new BigDecimal("10000"));
        when(reportService.obtenerValorizacionInventario()).thenReturn(response);

        ResponseEntity<InventoryValuationDto> result = controller.inventoryValuation();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(new BigDecimal("10000"), result.getBody().valorTotalInventario());
    }

    @Test
    void lowStockAlertsShouldReturnOk() {
        ReportController controller = new ReportController(reportService);
        UUID productId = UUID.randomUUID();
        LowStockAlertDto alert = new LowStockAlertDto(productId, "Taladro", 2, 5);
        when(reportService.obtenerAlertasStockBajo()).thenReturn(List.of(alert));

        ResponseEntity<List<LowStockAlertDto>> result = controller.lowStockAlerts();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals(2, result.getBody().get(0).stock());
    }
}
