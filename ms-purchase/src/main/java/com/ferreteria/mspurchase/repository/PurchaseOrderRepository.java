package com.ferreteria.mspurchase.repository;

import com.ferreteria.mspurchase.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, UUID> {

    List<PurchaseOrder> findAllByOrderByCreatedAtDesc();
}
