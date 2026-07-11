package com.ferreteria.msreport.controller;

import com.ferreteria.msreport.dto.InventoryValuationDto;
import com.ferreteria.msreport.dto.LowStockAlertDto;
import com.ferreteria.msreport.dto.SalesSummaryDto;
import com.ferreteria.msreport.dto.TopProductDto;
import com.ferreteria.msreport.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing read-only aggregated reports built from data in
 * ms-sales, ms-inventory and ms-product.
 */
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "Endpoints for read-only aggregated reports")
public class ReportController {

    private final ReportService reportService;

    /**
     * Returns the sales summary report.
     *
     * @return the sales summary
     */
    @Operation(
            summary = "Sales summary",
            description = "Returns the aggregated sales summary from ms-sales."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sales summary retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/sales-summary")
    public ResponseEntity<SalesSummaryDto> salesSummary() {
        return ResponseEntity.ok(reportService.obtenerResumenVentas());
    }

    /**
     * Returns the top-selling products report.
     *
     * @param limit maximum number of products to include
     * @return the list of top products ordered by quantity sold, descending
     */
    @Operation(
            summary = "Top products",
            description = "Returns the top-selling products ordered by quantity sold, descending."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Top products retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/top-products")
    public ResponseEntity<List<TopProductDto>> topProducts(
            @Parameter(description = "Maximum number of products to include")
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(reportService.obtenerTopProductos(limit));
    }

    /**
     * Returns the inventory valuation report.
     *
     * @return the inventory valuation report
     */
    @Operation(
            summary = "Inventory valuation",
            description = "Returns the inventory valuation report, crossing stock with product prices."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory valuation retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/inventory-valuation")
    public ResponseEntity<InventoryValuationDto> inventoryValuation() {
        return ResponseEntity.ok(reportService.obtenerValorizacionInventario());
    }

    /**
     * Returns the low stock alerts report.
     *
     * @return the list of low stock alerts
     */
    @Operation(
            summary = "Low stock alerts",
            description = "Returns the low stock alerts enriched with product names."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Low stock alerts retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/low-stock-alerts")
    public ResponseEntity<List<LowStockAlertDto>> lowStockAlerts() {
        return ResponseEntity.ok(reportService.obtenerAlertasStockBajo());
    }
}
