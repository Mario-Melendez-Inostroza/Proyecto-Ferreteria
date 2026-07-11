package com.ferreteria.bff.controller;

import com.ferreteria.bff.dto.PurchaseItemRequestDto;
import com.ferreteria.bff.dto.PurchaseItemResponseDto;
import com.ferreteria.bff.dto.PurchaseOrderRequestDto;
import com.ferreteria.bff.dto.PurchaseOrderResponseDto;
import com.ferreteria.bff.service.PurchaseBffService;
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
class PurchaseControllerTest {

    @Mock
    private PurchaseBffService purchaseBffService;

    @Test
    void findAllShouldReturnOk() {
        PurchaseController controller = new PurchaseController(purchaseBffService);
        when(purchaseBffService.findAll()).thenReturn(List.of(response(UUID.randomUUID(), "PENDING")));

        ResponseEntity<List<PurchaseOrderResponseDto>> result = controller.findAll();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void findByIdShouldReturnOk() {
        PurchaseController controller = new PurchaseController(purchaseBffService);
        UUID id = UUID.randomUUID();
        when(purchaseBffService.findById(id)).thenReturn(response(id, "PENDING"));

        ResponseEntity<PurchaseOrderResponseDto> result = controller.findById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(id, result.getBody().id());
    }

    @Test
    void createShouldReturnCreated() {
        PurchaseController controller = new PurchaseController(purchaseBffService);
        PurchaseOrderRequestDto request = new PurchaseOrderRequestDto(UUID.randomUUID(),
                List.of(new PurchaseItemRequestDto(UUID.randomUUID(), 2, new BigDecimal("10.00"))));
        when(purchaseBffService.create(any())).thenReturn(response(UUID.randomUUID(), "PENDING"));

        ResponseEntity<PurchaseOrderResponseDto> result = controller.create(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void confirmShouldReturnOk() {
        PurchaseController controller = new PurchaseController(purchaseBffService);
        UUID id = UUID.randomUUID();
        when(purchaseBffService.confirm(id)).thenReturn(response(id, "CONFIRMED"));

        ResponseEntity<PurchaseOrderResponseDto> result = controller.confirm(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("CONFIRMED", result.getBody().status());
    }

    @Test
    void cancelShouldReturnOk() {
        PurchaseController controller = new PurchaseController(purchaseBffService);
        UUID id = UUID.randomUUID();
        when(purchaseBffService.cancel(id)).thenReturn(response(id, "CANCELLED"));

        ResponseEntity<PurchaseOrderResponseDto> result = controller.cancel(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("CANCELLED", result.getBody().status());
    }

    private PurchaseOrderResponseDto response(UUID id, String status) {
        return new PurchaseOrderResponseDto(id, UUID.randomUUID(), status,
                List.of(new PurchaseItemResponseDto(UUID.randomUUID(), 2, new BigDecimal("10.00"))),
                new BigDecimal("20.00"), LocalDateTime.now(), null);
    }
}
