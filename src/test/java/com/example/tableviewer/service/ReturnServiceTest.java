package com.example.tableviewer.service;

import com.example.tableviewer.dto.ReturnRequestDTO;
import com.example.tableviewer.dto.ReturnResponseDTO;
import com.example.tableviewer.model.Return;
import com.example.tableviewer.repository.ReturnRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReturnServiceTest {

    @Mock
    private ReturnRepository repository;

    @Mock
    private InventoryService inventoryService;

    private ReturnService service;

    @BeforeEach
    void setUp() {
        service = new ReturnService(repository, inventoryService);
    }

    @Test
    void findAll_shouldReturnPageOfReturns() {
        Return ret = createTestReturn();
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(ret)));

        Page<ReturnResponseDTO> result = service.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("978-7-111-54742-1", result.getContent().get(0).getIsbn());
    }

    @Test
    void findById_shouldReturnReturnWhenExists() {
        Return ret = createTestReturn();
        when(repository.findById(1L)).thenReturn(Optional.of(ret));

        Optional<ReturnResponseDTO> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("978-7-111-54742-1", result.get().getIsbn());
    }

    @Test
    void findByIsbn_shouldReturnMatchingReturns() {
        Return ret1 = createTestReturn();
        Return ret2 = new Return();
        ret2.setId(2L);
        ret2.setIsbn("978-7-111-54742-1");
        ret2.setReaderId(2L);
        when(repository.findByIsbn("978-7-111-54742-1")).thenReturn(Arrays.asList(ret1, ret2));

        List<ReturnResponseDTO> result = service.findByIsbn("978-7-111-54742-1");

        assertEquals(2, result.size());
    }

    @Test
    void create_shouldSaveAndReturnReturn() {
        ReturnRequestDTO dto = new ReturnRequestDTO();
        dto.setIsbn("978-7-111-54742-1");
        dto.setReaderId(1L);
        dto.setReturnDate(LocalDate.of(2024, 2, 15));
        dto.setLocationId(1L);
        dto.setUserId(1L);

        // Mock inventory increase
        when(inventoryService.increaseQty(anyString(), anyLong(), anyInt())).thenReturn(true);

        Return savedReturn = createTestReturn();
        when(repository.save(any(Return.class))).thenReturn(savedReturn);

        ReturnResponseDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals("978-7-111-54742-1", result.getIsbn());
        verify(inventoryService, times(1)).increaseQty("978-7-111-54742-1", 1L, 1);
        verify(repository, times(1)).save(any(Return.class));
    }

    @Test
    void update_shouldUpdateAndReturnWhenExists() {
        Return existingReturn = createTestReturn();
        ReturnRequestDTO updateDto = new ReturnRequestDTO();
        updateDto.setIsbn("978-7-111-54742-1");
        updateDto.setReaderId(1L);
        updateDto.setReturnDate(LocalDate.of(2024, 2, 15));
        when(repository.findById(1L)).thenReturn(Optional.of(existingReturn));
        when(repository.save(any(Return.class))).thenReturn(existingReturn);

        Optional<ReturnResponseDTO> result = service.update(1L, updateDto);

        assertTrue(result.isPresent());
        verify(repository, times(1)).save(any(Return.class));
    }

    @Test
    void delete_shouldReturnTrueWhenExists() {
        when(repository.existsById(1L)).thenReturn(true);

        boolean result = service.delete(1L);

        assertTrue(result);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void delete_shouldReturnFalseWhenNotExists() {
        when(repository.existsById(999L)).thenReturn(false);

        boolean result = service.delete(999L);

        assertFalse(result);
        verify(repository, never()).deleteById(any());
    }

    private Return createTestReturn() {
        Return ret = new Return();
        ret.setId(1L);
        ret.setIsbn("978-7-111-54742-1");
        ret.setReaderId(1L);
        ret.setReturnDate(LocalDate.of(2024, 2, 15));
        ret.setLocationId(1L);
        ret.setUserId(1L);
        return ret;
    }
}
