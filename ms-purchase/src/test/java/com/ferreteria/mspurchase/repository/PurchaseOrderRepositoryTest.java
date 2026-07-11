package com.ferreteria.mspurchase.repository;

import com.ferreteria.mspurchase.model.PurchaseOrder;
import com.ferreteria.mspurchase.model.PurchaseOrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PurchaseOrderRepositoryTest {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Test
    void shouldSaveAndFindPurchaseOrderById() {
        UUID marker = UUID.randomUUID();
        PurchaseOrder order = PurchaseOrder.builder()
                .supplierId(marker)
                .status("PENDING")
                .build();
        PurchaseOrderItem item = PurchaseOrderItem.builder()
                .purchaseOrder(order)
                .productId(UUID.randomUUID())
                .quantity(2)
                .unitCost(new BigDecimal("25.50"))
                .build();
        order.setItems(List.of(item));

        PurchaseOrder saved = purchaseOrderRepository.save(order);

        Optional<PurchaseOrder> found = purchaseOrderRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(marker, found.get().getSupplierId());
        assertEquals(1, found.get().getItems().size());
    }

    @Test
    void shouldListOrdersOrderedByCreatedAtDesc() {
        UUID marker = UUID.randomUUID();
        PurchaseOrder order = PurchaseOrder.builder()
                .supplierId(marker)
                .status("PENDING")
                .build();
        purchaseOrderRepository.save(order);

        List<PurchaseOrder> result = purchaseOrderRepository.findAllByOrderByCreatedAtDesc();

        assertTrue(result.stream().anyMatch(o -> marker.equals(o.getSupplierId())));
    }
}
