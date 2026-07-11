package com.ferreteria.msreport.service;

import com.ferreteria.msreport.dto.InventoryValuationDto;
import com.ferreteria.msreport.dto.LowStockAlertDto;
import com.ferreteria.msreport.dto.SalesSummaryDto;
import com.ferreteria.msreport.dto.TopProductDto;

import java.util.List;

/**
 * Service that aggregates read-only reports from ms-sales, ms-inventory
 * and ms-product.
 */
public interface ReportService {

    /**
     * Builds the sales summary report.
     *
     * @return the sales summary
     */
    SalesSummaryDto obtenerResumenVentas();

    /**
     * Builds the top-selling products report.
     *
     * @param limit maximum number of products to include
     * @return the list of top products ordered by quantity sold, descending
     */
    List<TopProductDto> obtenerTopProductos(int limit);

    /**
     * Builds the inventory valuation report by crossing inventory stock with
     * product prices.
     *
     * @return the inventory valuation report
     */
    InventoryValuationDto obtenerValorizacionInventario();

    /**
     * Builds the low stock alerts report, enriched with product names.
     *
     * @return the list of low stock alerts
     */
    List<LowStockAlertDto> obtenerAlertasStockBajo();
}
