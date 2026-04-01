package com.example.tableviewer.service;

import com.example.tableviewer.dto.InventoryRequestDTO;
import com.example.tableviewer.dto.InventoryResponseDTO;
import com.example.tableviewer.model.Inventory;
import com.example.tableviewer.model.InventoryId;
import com.example.tableviewer.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository repository;

    private InventoryService service;

    @BeforeEach
    void setUp() {
        service = new InventoryService(repository);
    }

    @Test
    void findAll_shouldReturnPageOfInventories() {
        Inventory inventory = createTestInventory();
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(inventory)));

        Page<InventoryResponseDTO> result = service.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("978-7-111-54742-1", result.getContent().get(0).getIsbn());
    }

    @Test
    void findById_shouldReturnInventoryWhenExists() {
        Inventory inventory = createTestInventory();
        when(repository.findById(new InventoryId("978-7-111-54742-1", 1L))).thenReturn(Optional.of(inventory));

        Optional<InventoryResponseDTO> result = service.findById("978-7-111-54742-1", 1L);

        assertTrue(result.isPresent());
        assertEquals("978-7-111-54742-1", result.get().getIsbn());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotExists() {
        when(repository.findById(new InventoryId("invalid", 999L))).thenReturn(Optional.empty());

        Optional<InventoryResponseDTO> result = service.findById("invalid", 999L);

        assertFalse(result.isPresent());
    }

    @Test
    void findByIsbn_shouldReturnMatchingInventories() {
        Inventory inv1 = createTestInventory();
        Inventory inv2 = new Inventory();
        inv2.setId(new InventoryId("978-7-111-54742-1", 2L));
        inv2.setQty(50);
        when(repository.findByIsbn("978-7-111-54742-1")).thenReturn(Arrays.asList(inv1, inv2));

        List<InventoryResponseDTO> result = service.findByIsbn("978-7-111-54742-1");

        assertEquals(2, result.size());
    }

    @Test
    void create_shouldSaveAndReturnInventory() {
        InventoryRequestDTO dto = new InventoryRequestDTO();
        dto.setIsbn("978-7-111-54742-1");
        dto.setLocationId(1L);
        dto.setQty(100);
        dto.setUpdateDate(LocalDateTime.of(2024, 1, 15, 10, 30));

        Inventory savedInventory = createTestInventory();
        when(repository.save(any(Inventory.class))).thenReturn(savedInventory);

        InventoryResponseDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals("978-7-111-54742-1", result.getIsbn());
        verify(repository, times(1)).save(any(Inventory.class));
    }

    @Test
    void update_shouldUpdateAndReturnInventoryWhenExists() {
        Inventory existingInventory = createTestInventory();
        InventoryRequestDTO updateDto = new InventoryRequestDTO();
        updateDto.setQty(150);
        when(repository.findById(new InventoryId("978-7-111-54742-1", 1L))).thenReturn(Optional.of(existingInventory));
        when(repository.save(any(Inventory.class))).thenReturn(existingInventory);

        Optional<InventoryResponseDTO> result = service.update("978-7-111-54742-1", 1L, updateDto);

        assertTrue(result.isPresent());
        verify(repository, times(1)).save(any(Inventory.class));
    }

    @Test
    void update_shouldReturnEmptyWhenNotExists() {
        InventoryRequestDTO updateDto = new InventoryRequestDTO();
        when(repository.findById(new InventoryId("invalid", 999L))).thenReturn(Optional.empty());

        Optional<InventoryResponseDTO> result = service.update("invalid", 999L, updateDto);

        assertFalse(result.isPresent());
        verify(repository, never()).save(any(Inventory.class));
    }

    @Test
    void delete_shouldReturnTrueWhenExists() {
        InventoryId id = new InventoryId("978-7-111-54742-1", 1L);
        when(repository.existsById(id)).thenReturn(true);

        boolean result = service.delete("978-7-111-54742-1", 1L);

        assertTrue(result);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void delete_shouldReturnFalseWhenNotExists() {
        InventoryId id = new InventoryId("invalid", 999L);
        when(repository.existsById(id)).thenReturn(false);

        boolean result = service.delete("invalid", 999L);

        assertFalse(result);
        verify(repository, never()).deleteById(any());
    }

    private Inventory createTestInventory() {
        Inventory inventory = new Inventory();
        inventory.setId(new InventoryId("978-7-111-54742-1", 1L));
        inventory.setQty(100);
        inventory.setUpdateDate(LocalDateTime.of(2024, 1, 15, 10, 30));
        return inventory;
    }
}
