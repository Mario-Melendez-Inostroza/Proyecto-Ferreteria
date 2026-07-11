package com.ferreteria.bff.controller;

import com.ferreteria.bff.dto.MessageResponseDto;
import com.ferreteria.bff.dto.SupplierRequestDto;
import com.ferreteria.bff.dto.SupplierResponseDto;
import com.ferreteria.bff.service.SupplierBffService;
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
    private SupplierBffService supplierBffService;

    @Test
    void findAllShouldReturnOk() {
        SupplierController controller = new SupplierController(supplierBffService);
        SupplierResponseDto response = new SupplierResponseDto(UUID.randomUUID(), "Ferretero SA", "76.123.456-7",
                "Juan", "555-1234", "juan@ferretero.cl", "Calle 1", true, LocalDateTime.now());
        when(supplierBffService.findAll()).thenReturn(List.of(response));

        ResponseEntity<List<SupplierResponseDto>> result = controller.findAll();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void findByIdShouldReturnOk() {
        SupplierController controller = new SupplierController(supplierBffService);
        UUID id = UUID.randomUUID();
        SupplierResponseDto response = new SupplierResponseDto(id, "Ferretero SA", "76.123.456-7",
                "Juan", "555-1234", "juan@ferretero.cl", "Calle 1", true, LocalDateTime.now());
        when(supplierBffService.findById(id)).thenReturn(response);

        ResponseEntity<SupplierResponseDto> result = controller.findById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(id, result.getBody().id());
    }

    @Test
    void findByNameShouldReturnOk() {
        SupplierController controller = new SupplierController(supplierBffService);
        SupplierResponseDto response = new SupplierResponseDto(UUID.randomUUID(), "Ferretero SA", "76.123.456-7",
                "Juan", "555-1234", "juan@ferretero.cl", "Calle 1", true, LocalDateTime.now());
        when(supplierBffService.findByName("Ferre")).thenReturn(List.of(response));

        ResponseEntity<List<SupplierResponseDto>> result = controller.findByName("Ferre");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void createShouldReturnCreated() {
        SupplierController controller = new SupplierController(supplierBffService);
        SupplierRequestDto request = new SupplierRequestDto("Ferretero SA", "76.123.456-7", "Juan",
                "555-1234", "juan@ferretero.cl", "Calle 1");
        SupplierResponseDto response = new SupplierResponseDto(UUID.randomUUID(), "Ferretero SA", "76.123.456-7",
                "Juan", "555-1234", "juan@ferretero.cl", "Calle 1", true, LocalDateTime.now());
        when(supplierBffService.create(any())).thenReturn(response);

        ResponseEntity<SupplierResponseDto> result = controller.create(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void updateShouldReturnOk() {
        SupplierController controller = new SupplierController(supplierBffService);
        UUID id = UUID.randomUUID();
        SupplierRequestDto request = new SupplierRequestDto("Ferretero SA", "76.123.456-7", "Juan",
                "555-1234", "juan@ferretero.cl", "Calle 1");
        SupplierResponseDto response = new SupplierResponseDto(id, "Ferretero SA", "76.123.456-7",
                "Juan", "555-1234", "juan@ferretero.cl", "Calle 1", true, LocalDateTime.now());
        when(supplierBffService.update(any(), any())).thenReturn(response);

        ResponseEntity<SupplierResponseDto> result = controller.update(id, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(id, result.getBody().id());
    }

    @Test
    void deactivateShouldReturnOk() {
        SupplierController controller = new SupplierController(supplierBffService);
        MessageResponseDto response = new MessageResponseDto("Proveedor desactivado");
        when(supplierBffService.deactivate(any())).thenReturn(response);

        ResponseEntity<MessageResponseDto> result = controller.deactivate(UUID.randomUUID());

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }
}
