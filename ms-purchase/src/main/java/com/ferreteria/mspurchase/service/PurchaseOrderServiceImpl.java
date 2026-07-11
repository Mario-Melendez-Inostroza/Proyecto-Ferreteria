package com.ferreteria.mspurchase.service;

import com.ferreteria.mspurchase.client.InventoryClient;
import com.ferreteria.mspurchase.client.ProductClient;
import com.ferreteria.mspurchase.client.SupplierClient;
import com.ferreteria.mspurchase.dto.PurchaseItemRequestDto;
import com.ferreteria.mspurchase.dto.PurchaseItemResponseDto;
import com.ferreteria.mspurchase.dto.PurchaseOrderRequestDto;
import com.ferreteria.mspurchase.dto.PurchaseOrderResponseDto;
import com.ferreteria.mspurchase.exception.InvalidPurchaseStateException;
import com.ferreteria.mspurchase.exception.ProductNotFoundException;
import com.ferreteria.mspurchase.exception.PurchaseOrderNotFoundException;
import com.ferreteria.mspurchase.exception.SupplierNotFoundException;
import com.ferreteria.mspurchase.model.PurchaseOrder;
import com.ferreteria.mspurchase.model.PurchaseOrderItem;
import com.ferreteria.mspurchase.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Default {@link PurchaseOrderService} implementation backed by
 * {@link PurchaseOrderRepository} for persistence, and orchestrating
 * {@link SupplierClient} and {@link ProductClient} for reference data
 * validation, and {@link InventoryClient} to increase stock when a purchase
 * order is confirmed.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierClient supplierClient;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;

    @Override
    public PurchaseOrderResponseDto crear(PurchaseOrderRequestDto dto) {
        // Validar que el proveedor existe en ms-supplier
        try {
            supplierClient.buscarProveedor(dto.supplierId());
        } catch (Exception e) {
            throw new SupplierNotFoundException("Proveedor no encontrado: " + dto.supplierId());
        }

        // Validar que cada producto existe en ms-product
        for (PurchaseItemRequestDto item : dto.items()) {
            try {
                productClient.buscarProducto(item.productId());
            } catch (Exception e) {
                throw new ProductNotFoundException("Producto no encontrado: " + item.productId());
            }
        }

        PurchaseOrder order = PurchaseOrder.builder()
                .supplierId(dto.supplierId())
                .status("PENDING")
                .build();

        List<PurchaseOrderItem> items = dto.items().stream()
                .map(item -> PurchaseOrderItem.builder()
                        .purchaseOrder(order)
                        .productId(item.productId())
                        .quantity(item.quantity())
                        .unitCost(item.unitCost())
                        .build())
                .toList();
        order.setItems(items);

        PurchaseOrder saved = purchaseOrderRepository.save(order);
        log.info("Orden de compra creada: {}", saved.getId());

        return toResponseDto(saved);
    }

    @Override
    public List<PurchaseOrderResponseDto> listarTodas() {
        return purchaseOrderRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public PurchaseOrderResponseDto buscarPorId(UUID id) {
        PurchaseOrder order = obtenerOrden(id);
        return toResponseDto(order);
    }

    @Override
    public PurchaseOrderResponseDto confirmar(UUID id) {
        PurchaseOrder order = obtenerOrden(id);

        if (!"PENDING".equals(order.getStatus())) {
            throw new InvalidPurchaseStateException(
                    "La orden no está en estado PENDING: " + order.getStatus());
        }

        // Incrementar stock en ms-inventory por cada ítem antes de confirmar,
        // así no persistimos el cambio de estado si alguna llamada falla.
        try {
            for (PurchaseOrderItem item : order.getItems()) {
                inventoryClient.agregarStock(item.getProductId(), item.getQuantity());
            }
        } catch (Exception e) {
            log.error("Error al actualizar inventario al confirmar la orden {}: {}", id, e.getMessage());
            throw new InvalidPurchaseStateException(
                    "No se pudo actualizar el inventario para la orden: " + id);
        }

        order.setStatus("CONFIRMED");
        order.setConfirmedAt(LocalDateTime.now());
        PurchaseOrder saved = purchaseOrderRepository.save(order);
        log.info("Orden de compra confirmada: {}", saved.getId());

        return toResponseDto(saved);
    }

    @Override
    public PurchaseOrderResponseDto cancelar(UUID id) {
        PurchaseOrder order = obtenerOrden(id);

        if (!"PENDING".equals(order.getStatus())) {
            throw new InvalidPurchaseStateException(
                    "La orden no está en estado PENDING: " + order.getStatus());
        }

        order.setStatus("CANCELLED");
        PurchaseOrder saved = purchaseOrderRepository.save(order);
        log.info("Orden de compra cancelada: {}", saved.getId());

        return toResponseDto(saved);
    }

    private PurchaseOrder obtenerOrden(UUID id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new PurchaseOrderNotFoundException("Orden de compra no encontrada: " + id));
    }

    private PurchaseOrderResponseDto toResponseDto(PurchaseOrder order) {
        List<PurchaseItemResponseDto> items = order.getItems().stream()
                .map(item -> new PurchaseItemResponseDto(item.getProductId(), item.getQuantity(), item.getUnitCost()))
                .toList();

        BigDecimal totalCost = order.getItems().stream()
                .map(item -> item.getUnitCost().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new PurchaseOrderResponseDto(
                order.getId(),
                order.getSupplierId(),
                order.getStatus(),
                items,
                totalCost,
                order.getCreatedAt(),
                order.getConfirmedAt());
    }
}
