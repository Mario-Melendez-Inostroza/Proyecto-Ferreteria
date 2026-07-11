package com.ferreteria.bff.service;

import com.ferreteria.bff.client.SupplierClient;
import com.ferreteria.bff.dto.MessageResponseDto;
import com.ferreteria.bff.dto.SupplierRequestDto;
import com.ferreteria.bff.dto.SupplierResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupplierBffServiceTest {

    @Mock
    private SupplierClient supplierClient;

    @Test
    void findAllShouldDelegateToClient() {
        SupplierBffService service = new SupplierBffService(supplierClient);
        when(supplierClient.findAll()).thenReturn(List.of(response(UUID.randomUUID())));

        List<SupplierResponseDto> result = service.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findByIdShouldDelegateToClient() {
        SupplierBffService service = new SupplierBffService(supplierClient);
        UUID id = UUID.randomUUID();
        when(supplierClient.findById(id)).thenReturn(response(id));

        SupplierResponseDto result = service.findById(id);

        assertEquals(id, result.id());
    }

    @Test
    void findByNameShouldDelegateToClient() {
        SupplierBffService service = new SupplierBffService(supplierClient);
        when(supplierClient.findByName("Ferre")).thenReturn(List.of(response(UUID.randomUUID())));

        List<SupplierResponseDto> result = service.findByName("Ferre");

        assertEquals(1, result.size());
    }

    @Test
    void createShouldDelegateToClient() {
        SupplierBffService service = new SupplierBffService(supplierClient);
        SupplierRequestDto request = request();
        when(supplierClient.create(request)).thenReturn(response(UUID.randomUUID()));

        SupplierResponseDto result = service.create(request);

        assertEquals("Ferretero SA", result.name());
    }

    @Test
    void updateShouldDelegateToClient() {
        SupplierBffService service = new SupplierBffService(supplierClient);
        UUID id = UUID.randomUUID();
        SupplierRequestDto request = request();
        when(supplierClient.update(id, request)).thenReturn(response(id));

        SupplierResponseDto result = service.update(id, request);

        assertEquals(id, result.id());
    }

    @Test
    void deactivateShouldDelegateToClient() {
        SupplierBffService service = new SupplierBffService(supplierClient);
        UUID id = UUID.randomUUID();
        when(supplierClient.deactivate(id)).thenReturn(new MessageResponseDto("ok"));

        MessageResponseDto result = service.deactivate(id);

        assertEquals("ok", result.message());
    }

    private SupplierRequestDto request() {
        return new SupplierRequestDto("Ferretero SA", "76.123.456-7", "Juan", "555-1234",
                "juan@ferretero.cl", "Calle 1");
    }

    private SupplierResponseDto response(UUID id) {
        return new SupplierResponseDto(id, "Ferretero SA", "76.123.456-7", "Juan", "555-1234",
                "juan@ferretero.cl", "Calle 1", true, LocalDateTime.now());
    }
}
