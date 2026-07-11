package com.ferreteria.mssupplier.repository;

import com.ferreteria.mssupplier.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SupplierRepository extends JpaRepository<Supplier, UUID> {
    List<Supplier> findByActiveTrue();
    List<Supplier> findByNameContainingIgnoreCaseAndActiveTrue(String name);
    boolean existsByRut(String rut);
}
