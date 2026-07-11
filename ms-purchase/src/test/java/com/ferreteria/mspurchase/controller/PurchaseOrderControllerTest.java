package com.ferreteria.mspurchase.controller;

import com.ferreteria.mspurchase.dto.PurchaseItemRequestDto;
import com.ferreteria.mspurchase.dto.PurchaseItemResponseDto;
import com.ferreteria.mspurchase.dto.PurchaseOrderRequestDto;
import com.ferreteria.mspurchase.dto.PurchaseOrderResponseDto;
import com.ferreteria.mspurchase.service.PurchaseOrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseOrderControllerTest {

    @Mock
    private PurchaseOrderService purchaseOrderService;

    @Test
    void crearShouldReturnCreated() {
        PurchaseOrderController controller = new PurchaseOrderController(purchaseOrderService);
        UUID supplierId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        PurchaseOrderRequestDto request = new PurchaseOrderRequestDto(supplierId,
                List.of(new PurchaseItemRequestDto(productId, 5, new BigDecimal("100.00"))));
        PurchaseOrderResponseDto response = new PurchaseOrderResponseDto(UUID.randomUUID(), supplierId, "PENDING",
                List.of(new PurchaseItemResponseDto(productId, 5, new BigDecimal("100.00"))),
                new BigDecimal("500.00"), LocalDateTime.now(), null);
        when(purchaseOrderService.crear(any())).thenReturn(response);

        ResponseEntity<PurchaseOrderResponseDto> result = controller.crear(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("PENDING", result.getBody().status());
    }

    @Test
    void listarTodasShouldReturnOk() {
        PurchaseOrderController controller = new PurchaseOrderController(purchaseOrderService);
        PurchaseOrderResponseDto response = new PurchaseOrderResponseDto(UUID.randomUUID(), UUID.randomUUID(),
                "PENDING", List.of(), BigDecimal.ZERO, LocalDateTime.now(), null);
        when(purchaseOrderService.listarTodas()).thenReturn(List.of(response));

        ResponseEntity<List<PurchaseOrderResponseDto>> result = controller.listarTodas();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void buscarPorIdShouldReturnOk() {
        PurchaseOrderController controller = new PurchaseOrderController(purchaseOrderService);
        UUID id = UUID.randomUUID();
        PurchaseOrderResponseDto response = new PurchaseOrderResponseDto(id, UUID.randomUUID(), "PENDING",
                List.of(), BigDecimal.ZERO, LocalDateTime.now(), null);
        when(purchaseOrderService.buscarPorId(id)).thenReturn(response);

        ResponseEntity<PurchaseOrderResponseDto> result = controller.buscarPorId(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(id, result.getBody().id());
    }

    @Test
    void confirmarShouldReturnOk() {
        PurchaseOrderController controller = new PurchaseOrderController(purchaseOrderService);
        UUID id = UUID.randomUUID();
        PurchaseOrderResponseDto response = new PurchaseOrderResponseDto(id, UUID.randomUUID(), "CONFIRMED",
                List.of(), BigDecimal.ZERO, LocalDateTime.now(), LocalDateTime.now());
        when(purchaseOrderService.confirmar(id)).thenReturn(response);

        ResponseEntity<PurchaseOrderResponseDto> result = controller.confirmar(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("CONFIRMED", result.getBody().status());
    }

    @Test
    void cancelarShouldReturnOk() {
        PurchaseOrderController controller = new PurchaseOrderController(purchaseOrderService);
        UUID id = UUID.randomUUID();
        PurchaseOrderResponseDto response = new PurchaseOrderResponseDto(id, UUID.randomUUID(), "CANCELLED",
                List.of(), BigDecimal.ZERO, LocalDateTime.now(), null);
        when(purchaseOrderService.cancelar(id)).thenReturn(response);

        ResponseEntity<PurchaseOrderResponseDto> result = controller.cancelar(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("CANCELLED", result.getBody().status());
    }
}
