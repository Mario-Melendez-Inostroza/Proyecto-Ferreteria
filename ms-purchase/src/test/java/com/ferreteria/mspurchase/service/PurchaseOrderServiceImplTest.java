package com.ferreteria.mspurchase.service;

import com.ferreteria.mspurchase.client.InventoryClient;
import com.ferreteria.mspurchase.client.ProductClient;
import com.ferreteria.mspurchase.client.SupplierClient;
import com.ferreteria.mspurchase.dto.PurchaseItemRequestDto;
import com.ferreteria.mspurchase.dto.PurchaseOrderRequestDto;
import com.ferreteria.mspurchase.dto.PurchaseOrderResponseDto;
import com.ferreteria.mspurchase.dto.ProductDto;
import com.ferreteria.mspurchase.dto.SupplierDto;
import com.ferreteria.mspurchase.exception.InvalidPurchaseStateException;
import com.ferreteria.mspurchase.exception.PurchaseOrderNotFoundException;
import com.ferreteria.mspurchase.exception.SupplierNotFoundException;
import com.ferreteria.mspurchase.model.PurchaseOrder;
import com.ferreteria.mspurchase.model.PurchaseOrderItem;
import com.ferreteria.mspurchase.repository.PurchaseOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseOrderServiceImplTest {

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;

    @Mock
    private SupplierClient supplierClient;

    @Mock
    private ProductClient productClient;

    @Mock
    private InventoryClient inventoryClient;

    @Test
    void crearShouldReturnPendingOrderWhenFlowSucceeds() {
        PurchaseOrderServiceImpl service = new PurchaseOrderServiceImpl(purchaseOrderRepository, supplierClient,
                productClient, inventoryClient);
        UUID supplierId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        PurchaseOrderRequestDto request = new PurchaseOrderRequestDto(supplierId,
                List.of(new PurchaseItemRequestDto(productId, 3, new BigDecimal("10.00"))));
        when(supplierClient.buscarProveedor(supplierId)).thenReturn(new SupplierDto(supplierId, "ACME", true));
        when(productClient.buscarProducto(productId)).thenReturn(new ProductDto(productId, "Martillo", true));
        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenAnswer(inv -> inv.getArgument(0));

        PurchaseOrderResponseDto result = service.crear(request);

        assertNotNull(result);
        assertEquals("PENDING", result.status());
        assertEquals(new BigDecimal("30.00"), result.totalCost());
    }

    @Test
    void crearShouldThrowWhenSupplierIsMissing() {
        PurchaseOrderServiceImpl service = new PurchaseOrderServiceImpl(purchaseOrderRepository, supplierClient,
                productClient, inventoryClient);
        UUID supplierId = UUID.randomUUID();
        PurchaseOrderRequestDto request = new PurchaseOrderRequestDto(supplierId,
                List.of(new PurchaseItemRequestDto(UUID.randomUUID(), 1, new BigDecimal("10.00"))));
        when(supplierClient.buscarProveedor(supplierId)).thenThrow(new RuntimeException("not found"));

        SupplierNotFoundException ex = assertThrows(SupplierNotFoundException.class, () -> service.crear(request));

        assertNotNull(ex.getMessage());
    }

    @Test
    void listarTodasShouldReturnOrders() {
        PurchaseOrderServiceImpl service = new PurchaseOrderServiceImpl(purchaseOrderRepository, supplierClient,
                productClient, inventoryClient);
        PurchaseOrder order = pendingOrder();
        when(purchaseOrderRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(order));

        List<PurchaseOrderResponseDto> result = service.listarTodas();

        assertEquals(1, result.size());
    }

    @Test
    void buscarPorIdShouldThrowWhenMissing() {
        PurchaseOrderServiceImpl service = new PurchaseOrderServiceImpl(purchaseOrderRepository, supplierClient,
                productClient, inventoryClient);
        UUID id = UUID.randomUUID();
        when(purchaseOrderRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PurchaseOrderNotFoundException.class, () -> service.buscarPorId(id));
    }

    @Test
    void confirmarShouldUpdateInventoryForEveryItemAndMarkConfirmed() {
        PurchaseOrderServiceImpl service = new PurchaseOrderServiceImpl(purchaseOrderRepository, supplierClient,
                productClient, inventoryClient);
        PurchaseOrder order = pendingOrder();
        UUID id = order.getId();
        when(purchaseOrderRepository.findById(id)).thenReturn(Optional.of(order));
        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenAnswer(inv -> inv.getArgument(0));

        PurchaseOrderResponseDto result = service.confirmar(id);

        assertEquals("CONFIRMED", result.status());
        assertNotNull(result.confirmedAt());
        verify(inventoryClient, times(order.getItems().size())).agregarStock(any(), any());
    }

    @Test
    void confirmarShouldThrowWhenOrderIsNotPending() {
        PurchaseOrderServiceImpl service = new PurchaseOrderServiceImpl(purchaseOrderRepository, supplierClient,
                productClient, inventoryClient);
        PurchaseOrder order = pendingOrder();
        order.setStatus("CONFIRMED");
        UUID id = order.getId();
        when(purchaseOrderRepository.findById(id)).thenReturn(Optional.of(order));

        assertThrows(InvalidPurchaseStateException.class, () -> service.confirmar(id));
    }

    @Test
    void cancelarShouldMarkOrderCancelled() {
        PurchaseOrderServiceImpl service = new PurchaseOrderServiceImpl(purchaseOrderRepository, supplierClient,
                productClient, inventoryClient);
        PurchaseOrder order = pendingOrder();
        UUID id = order.getId();
        when(purchaseOrderRepository.findById(id)).thenReturn(Optional.of(order));
        when(purchaseOrderRepository.save(any(PurchaseOrder.class))).thenAnswer(inv -> inv.getArgument(0));

        PurchaseOrderResponseDto result = service.cancelar(id);

        assertEquals("CANCELLED", result.status());
    }

    @Test
    void cancelarShouldThrowWhenOrderIsNotPending() {
        PurchaseOrderServiceImpl service = new PurchaseOrderServiceImpl(purchaseOrderRepository, supplierClient,
                productClient, inventoryClient);
        PurchaseOrder order = pendingOrder();
        order.setStatus("CONFIRMED");
        UUID id = order.getId();
        when(purchaseOrderRepository.findById(id)).thenReturn(Optional.of(order));

        assertThrows(InvalidPurchaseStateException.class, () -> service.cancelar(id));
    }

    private PurchaseOrder pendingOrder() {
        PurchaseOrder order = PurchaseOrder.builder()
                .id(UUID.randomUUID())
                .supplierId(UUID.randomUUID())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();
        PurchaseOrderItem item = PurchaseOrderItem.builder()
                .id(UUID.randomUUID())
                .purchaseOrder(order)
                .productId(UUID.randomUUID())
                .quantity(4)
                .unitCost(new BigDecimal("15.00"))
                .build();
        order.setItems(List.of(item));
        return order;
    }
}
