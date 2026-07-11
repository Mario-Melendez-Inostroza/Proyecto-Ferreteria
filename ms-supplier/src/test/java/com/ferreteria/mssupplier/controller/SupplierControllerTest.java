package com.ferreteria.mssupplier.controller;

import com.ferreteria.mssupplier.dto.MessageResponseDto;
import com.ferreteria.mssupplier.dto.SupplierRequestDto;
import com.ferreteria.mssupplier.dto.SupplierResponseDto;
import com.ferreteria.mssupplier.service.SupplierService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupplierControllerTest {

    @Mock
    private SupplierService supplierService;

    @Test
    void crearShouldReturnCreated() {
        SupplierController controller = new SupplierController(supplierService);
        SupplierRequestDto request = new SupplierRequestDto("Ferretería El Tornillo", "76.111.222-3",
                "Juan Pérez", "+56 9 1111 2222", "contacto@eltornillo.cl", "Av. Industrial 450");
        SupplierResponseDto response = new SupplierResponseDto(UUID.randomUUID(), request.name(), request.rut(),
                request.contactName(), request.phone(), request.email(), request.address(), true,
                LocalDateTime.now());
        when(supplierService.crear(any())).thenReturn(response);

        ResponseEntity<SupplierResponseDto> result = controller.crear(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Ferretería El Tornillo", result.getBody().name());
    }

    @Test
    void listarShouldReturnOk() {
        SupplierController controller = new SupplierController(supplierService);
        SupplierResponseDto response = new SupplierResponseDto(UUID.randomUUID(), "Distribuidora Maderas",
                "77.222.333-4", "María Soto", "+56 9 2222 3333", "ventas@maderas.cl", "Camino Real 1200", true,
                LocalDateTime.now());
        when(supplierService.listarActivos()).thenReturn(List.of(response));

        ResponseEntity<List<SupplierResponseDto>> result = controller.listar();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void buscarPorIdShouldReturnOk() {
        SupplierController controller = new SupplierController(supplierService);
        UUID id = UUID.randomUUID();
        SupplierResponseDto response = new SupplierResponseDto(id, "Importadora Andina", "78.333.444-5",
                "Pedro Ramírez", "+56 9 3333 4444", "info@andina.cl", "Los Aromos 890", true, LocalDateTime.now());
        when(supplierService.buscarPorId(id)).thenReturn(response);

        ResponseEntity<SupplierResponseDto> result = controller.buscarPorId(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(id, result.getBody().id());
    }

    @Test
    void buscarPorNombreShouldReturnOk() {
        SupplierController controller = new SupplierController(supplierService);
        SupplierResponseDto response = new SupplierResponseDto(UUID.randomUUID(), "Ferretería El Tornillo",
                "76.111.222-3", "Juan Pérez", "+56 9 1111 2222", "contacto@eltornillo.cl", "Av. Industrial 450",
                true, LocalDateTime.now());
        when(supplierService.buscarPorNombre("Tornillo")).thenReturn(List.of(response));

        ResponseEntity<List<SupplierResponseDto>> result = controller.buscarPorNombre("Tornillo");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void actualizarShouldReturnOk() {
        SupplierController controller = new SupplierController(supplierService);
        UUID id = UUID.randomUUID();
        SupplierRequestDto request = new SupplierRequestDto("Ferretería El Tornillo Ltda.", "76.111.222-3",
                "Juan Pérez", "+56 9 1111 2222", "contacto@eltornillo.cl", "Av. Industrial 450");
        SupplierResponseDto response = new SupplierResponseDto(id, request.name(), request.rut(),
                request.contactName(), request.phone(), request.email(), request.address(), true,
                LocalDateTime.now());
        when(supplierService.actualizar(any(), any())).thenReturn(response);

        ResponseEntity<SupplierResponseDto> result = controller.actualizar(id, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Ferretería El Tornillo Ltda.", result.getBody().name());
    }

    @Test
    void desactivarShouldReturnOk() {
        SupplierController controller = new SupplierController(supplierService);
        UUID id = UUID.randomUUID();
        MessageResponseDto response = new MessageResponseDto("Proveedor desactivado: Ferretería El Tornillo");
        when(supplierService.desactivar(id)).thenReturn(response);

        ResponseEntity<MessageResponseDto> result = controller.desactivar(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Proveedor desactivado: Ferretería El Tornillo", result.getBody().message());
    }
}
