package com.ferreteria.bff.service;

import com.ferreteria.bff.client.PurchaseClient;
import com.ferreteria.bff.dto.PurchaseItemRequestDto;
import com.ferreteria.bff.dto.PurchaseItemResponseDto;
import com.ferreteria.bff.dto.PurchaseOrderRequestDto;
import com.ferreteria.bff.dto.PurchaseOrderResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseBffServiceTest {

    @Mock
    private PurchaseClient purchaseClient;

    @Test
    void findAllShouldDelegateToClient() {
        PurchaseBffService service = new PurchaseBffService(purchaseClient);
        when(purchaseClient.findAll()).thenReturn(List.of(response(UUID.randomUUID(), "PENDING")));

        List<PurchaseOrderResponseDto> result = service.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findByIdShouldDelegateToClient() {
        PurchaseBffService service = new PurchaseBffService(purchaseClient);
        UUID id = UUID.randomUUID();
        when(purchaseClient.findById(id)).thenReturn(response(id, "PENDING"));

        PurchaseOrderResponseDto result = service.findById(id);

        assertEquals(id, result.id());
    }

    @Test
    void createShouldDelegateToClient() {
        PurchaseBffService service = new PurchaseBffService(purchaseClient);
        PurchaseOrderRequestDto request = new PurchaseOrderRequestDto(UUID.randomUUID(),
                List.of(new PurchaseItemRequestDto(UUID.randomUUID(), 2, new BigDecimal("10.00"))));
        when(purchaseClient.create(request)).thenReturn(response(UUID.randomUUID(), "PENDING"));

        PurchaseOrderResponseDto result = service.create(request);

        assertEquals("PENDING", result.status());
    }

    @Test
    void confirmShouldDelegateToClient() {
        PurchaseBffService service = new PurchaseBffService(purchaseClient);
        UUID id = UUID.randomUUID();
        when(purchaseClient.confirm(id)).thenReturn(response(id, "CONFIRMED"));

        PurchaseOrderResponseDto result = service.confirm(id);

        assertEquals("CONFIRMED", result.status());
    }

    @Test
    void cancelShouldDelegateToClient() {
        PurchaseBffService service = new PurchaseBffService(purchaseClient);
        UUID id = UUID.randomUUID();
        when(purchaseClient.cancel(id)).thenReturn(response(id, "CANCELLED"));

        PurchaseOrderResponseDto result = service.cancel(id);

        assertEquals("CANCELLED", result.status());
    }

    private PurchaseOrderResponseDto response(UUID id, String status) {
        return new PurchaseOrderResponseDto(id, UUID.randomUUID(), status,
                List.of(new PurchaseItemResponseDto(UUID.randomUUID(), 2, new BigDecimal("10.00"))),
                new BigDecimal("20.00"), LocalDateTime.now(), null);
    }
}
