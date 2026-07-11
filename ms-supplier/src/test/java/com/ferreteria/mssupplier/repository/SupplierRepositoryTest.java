package com.ferreteria.mssupplier.repository;

import com.ferreteria.mssupplier.model.Supplier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class SupplierRepositoryTest {

    @Autowired
    private SupplierRepository supplierRepository;

    @Test
    void shouldFindActiveSuppliers() {
        String rut = "R" + UUID.randomUUID().toString().substring(0, 15);
        Supplier supplier = Supplier.builder()
                .name("Ferretería Repository Test")
                .rut(rut)
                .active(true)
                .build();

        supplierRepository.save(supplier);

        List<Supplier> result = supplierRepository.findByActiveTrue();

        assertTrue(result.stream().anyMatch(s -> s.getRut().equals(rut)));
    }

    @Test
    void shouldFindByNameContainingIgnoreCaseAndActiveTrue() {
        String uniqueMarker = "Marker" + UUID.randomUUID().toString().substring(0, 8);
        String rut = "R" + UUID.randomUUID().toString().substring(0, 15);
        Supplier supplier = Supplier.builder()
                .name("Ferretería " + uniqueMarker)
                .rut(rut)
                .active(true)
                .build();

        supplierRepository.save(supplier);

        List<Supplier> result = supplierRepository
                .findByNameContainingIgnoreCaseAndActiveTrue(uniqueMarker.toLowerCase());

        assertEquals(1, result.size());
        assertEquals(rut, result.get(0).getRut());
    }

    @Test
    void shouldCheckExistsByRut() {
        String rut = "R" + UUID.randomUUID().toString().substring(0, 15);
        Supplier supplier = Supplier.builder()
                .name("Ferretería Existencia")
                .rut(rut)
                .active(true)
                .build();

        supplierRepository.save(supplier);

        assertTrue(supplierRepository.existsByRut(rut));
        assertFalse(supplierRepository.existsByRut("X" + UUID.randomUUID().toString().substring(0, 15)));
    }
}
