package com.ferreteria.msreport.service;

import com.ferreteria.msreport.client.InventoryClient;
import com.ferreteria.msreport.client.ProductClient;
import com.ferreteria.msreport.client.SalesClient;
import com.ferreteria.msreport.dto.InventoryDto;
import com.ferreteria.msreport.dto.InventoryValuationDto;
import com.ferreteria.msreport.dto.InventoryValuationItemDto;
import com.ferreteria.msreport.dto.LowStockAlertDto;
import com.ferreteria.msreport.dto.ProductDto;
import com.ferreteria.msreport.dto.ResumenVentasDto;
import com.ferreteria.msreport.dto.SalesSummaryDto;
import com.ferreteria.msreport.dto.TopProductDto;
import com.ferreteria.msreport.dto.VentaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final SalesClient salesClient;
    private final InventoryClient inventoryClient;
    private final ProductClient productClient;

    /**
     * Builds the sales summary report by delegating to ms-sales.
     *
     * @return the sales summary
     */
    @Override
    public SalesSummaryDto obtenerResumenVentas() {
        log.info("Generando reporte de resumen de ventas");
        ResumenVentasDto resumen = salesClient.obtenerResumen();
        return new SalesSummaryDto(resumen.totalVentas(), resumen.montoTotal());
    }

    /**
     * Builds the top-selling products report by grouping all sales by
     * product id, summing quantities, and taking the top N by quantity sold.
     *
     * @param limit maximum number of products to include
     * @return the list of top products ordered by quantity sold, descending
     */
    @Override
    public List<TopProductDto> obtenerTopProductos(int limit) {
        log.info("Generando reporte de top {} productos", limit);
        List<VentaDto> ventas = salesClient.listarVentas();

        Map<UUID, String> nombresPorProducto = ventas.stream()
                .collect(Collectors.toMap(
                        VentaDto::productId,
                        VentaDto::nombreProducto,
                        (existing, replacement) -> existing));

        Map<UUID, Long> cantidadPorProducto = ventas.stream()
                .collect(Collectors.groupingBy(
                        VentaDto::productId,
                        Collectors.summingLong(v -> v.cantidad() == null ? 0L : v.cantidad())));

        return cantidadPorProducto.entrySet().stream()
                .sorted(Map.Entry.<UUID, Long>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> new TopProductDto(
                        entry.getKey(),
                        nombresPorProducto.get(entry.getKey()),
                        entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Builds the inventory valuation report by crossing inventory stock with
     * product prices. Inventory items without a matching product are
     * excluded from the result.
     *
     * @return the inventory valuation report
     */
    @Override
    public InventoryValuationDto obtenerValorizacionInventario() {
        log.info("Generando reporte de valorizacion de inventario");
        List<InventoryDto> inventario = inventoryClient.listarInventario();
        List<ProductDto> productos = productClient.listarProductos();

        Map<UUID, ProductDto> productosPorId = productos.stream()
                .collect(Collectors.toMap(ProductDto::id, p -> p, (existing, replacement) -> existing));

        List<InventoryValuationItemDto> items = inventario.stream()
                .map(inv -> {
                    ProductDto producto = productosPorId.get(inv.productId());
                    if (producto == null) {
                        log.warn("Producto {} presente en inventario pero no encontrado en ms-product, se excluye",
                                inv.productId());
                        return null;
                    }
                    BigDecimal valorTotal = producto.precio().multiply(BigDecimal.valueOf(inv.stock()));
                    return new InventoryValuationItemDto(
                            inv.productId(),
                            producto.nombre(),
                            inv.stock(),
                            producto.precio(),
                            valorTotal);
                })
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());

        BigDecimal valorTotalInventario = items.stream()
                .map(InventoryValuationItemDto::valorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new InventoryValuationDto(items, valorTotalInventario);
    }

    /**
     * Builds the low stock alerts report, enriching each alert with the
     * product name. If a product lookup fails, a placeholder name is used
     * and the failure is logged, without failing the whole report.
     *
     * @return the list of low stock alerts
     */
    @Override
    public List<LowStockAlertDto> obtenerAlertasStockBajo() {
        log.info("Generando reporte de alertas de stock bajo");
        List<InventoryDto> stockBajo = inventoryClient.listarStockBajo();

        return stockBajo.stream()
                .map(inv -> {
                    String nombre;
                    try {
                        ProductDto producto = productClient.buscarProducto(inv.productId());
                        nombre = producto != null ? producto.nombre() : "Producto desconocido";
                    } catch (Exception e) {
                        log.warn("No se pudo obtener el producto {} para la alerta de stock bajo: {}",
                                inv.productId(), e.getMessage());
                        nombre = "Producto desconocido";
                    }
                    return new LowStockAlertDto(inv.productId(), nombre, inv.stock(), inv.stockMinimo());
                })
                .collect(Collectors.toList());
    }
}
