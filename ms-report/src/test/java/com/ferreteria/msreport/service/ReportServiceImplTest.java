package com.ferreteria.msreport.service;

import com.ferreteria.msreport.client.InventoryClient;
import com.ferreteria.msreport.client.ProductClient;
import com.ferreteria.msreport.client.SalesClient;
import com.ferreteria.msreport.dto.InventoryDto;
import com.ferreteria.msreport.dto.InventoryValuationDto;
import com.ferreteria.msreport.dto.LowStockAlertDto;
import com.ferreteria.msreport.dto.ProductDto;
import com.ferreteria.msreport.dto.ResumenVentasDto;
import com.ferreteria.msreport.dto.SalesSummaryDto;
import com.ferreteria.msreport.dto.TopProductDto;
import com.ferreteria.msreport.dto.VentaDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    private SalesClient salesClient;

    @Mock
    private InventoryClient inventoryClient;

    @Mock
    private ProductClient productClient;

    @Test
    void obtenerResumenVentasShouldDelegateToSalesClient() {
        ReportServiceImpl service = new ReportServiceImpl(salesClient, inventoryClient, productClient);
        when(salesClient.obtenerResumen()).thenReturn(new ResumenVentasDto(10L, new BigDecimal("100000")));

        SalesSummaryDto result = service.obtenerResumenVentas();

        assertEquals(10L, result.totalVentas());
        assertEquals(new BigDecimal("100000"), result.montoTotal());
    }

    @Test
    void obtenerTopProductosShouldGroupAndOrderDescending() {
        ReportServiceImpl service = new ReportServiceImpl(salesClient, inventoryClient, productClient);
        UUID productA = UUID.randomUUID();
        UUID productB = UUID.randomUUID();
        UUID productC = UUID.randomUUID();

        List<VentaDto> ventas = List.of(
                new VentaDto(UUID.randomUUID(), productA, "Martillo", 3, new BigDecimal("30000")),
                new VentaDto(UUID.randomUUID(), productA, "Martillo", 2, new BigDecimal("20000")),
                new VentaDto(UUID.randomUUID(), productB, "Taladro", 10, new BigDecimal("500000")),
                new VentaDto(UUID.randomUUID(), productC, "Destornillador", 1, new BigDecimal("2000"))
        );
        when(salesClient.listarVentas()).thenReturn(ventas);

        List<TopProductDto> result = service.obtenerTopProductos(2);

        assertEquals(2, result.size());
        assertEquals(productB, result.get(0).productId());
        assertEquals(10L, result.get(0).cantidadVendida());
        assertEquals(productA, result.get(1).productId());
        assertEquals(5L, result.get(1).cantidadVendida());
    }

    @Test
    void obtenerValorizacionInventarioShouldCrossStockAndPriceAndSkipUnmatched() {
        ReportServiceImpl service = new ReportServiceImpl(salesClient, inventoryClient, productClient);
        UUID productA = UUID.randomUUID();
        UUID productUnmatched = UUID.randomUUID();

        List<InventoryDto> inventario = List.of(
                new InventoryDto(UUID.randomUUID(), productA, 10, 5, false),
                new InventoryDto(UUID.randomUUID(), productUnmatched, 4, 2, false)
        );
        List<ProductDto> productos = List.of(
                new ProductDto(productA, "Taladro", new BigDecimal("1000"), "Herramientas", true)
        );
        when(inventoryClient.listarInventario()).thenReturn(inventario);
        when(productClient.listarProductos()).thenReturn(productos);

        InventoryValuationDto result = service.obtenerValorizacionInventario();

        assertEquals(1, result.items().size());
        assertEquals(productA, result.items().get(0).productId());
        assertEquals(new BigDecimal("10000"), result.items().get(0).valorTotal());
        assertEquals(new BigDecimal("10000"), result.valorTotalInventario());
    }

    @Test
    void obtenerAlertasStockBajoShouldEnrichWithProductName() {
        ReportServiceImpl service = new ReportServiceImpl(salesClient, inventoryClient, productClient);
        UUID productA = UUID.randomUUID();

        when(inventoryClient.listarStockBajo())
                .thenReturn(List.of(new InventoryDto(UUID.randomUUID(), productA, 2, 5, true)));
        when(productClient.buscarProducto(productA))
                .thenReturn(new ProductDto(productA, "Taladro", new BigDecimal("1000"), "Herramientas", true));

        List<LowStockAlertDto> result = service.obtenerAlertasStockBajo();

        assertEquals(1, result.size());
        assertEquals("Taladro", result.get(0).nombre());
        assertEquals(2, result.get(0).stock());
        assertEquals(5, result.get(0).stockMinimo());
    }

    @Test
    void obtenerAlertasStockBajoShouldUsePlaceholderWhenLookupFails() {
        ReportServiceImpl service = new ReportServiceImpl(salesClient, inventoryClient, productClient);
        UUID productA = UUID.randomUUID();

        when(inventoryClient.listarStockBajo())
                .thenReturn(List.of(new InventoryDto(UUID.randomUUID(), productA, 1, 5, true)));
        when(productClient.buscarProducto(productA)).thenThrow(new RuntimeException("not found"));

        List<LowStockAlertDto> result = service.obtenerAlertasStockBajo();

        assertEquals(1, result.size());
        assertEquals("Producto desconocido", result.get(0).nombre());
        assertTrue(result.get(0).stock() == 1);
    }
}
