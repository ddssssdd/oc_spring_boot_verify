package com.example.tableviewer.service;

import com.example.tableviewer.dto.BorrowRequestDTO;
import com.example.tableviewer.dto.BorrowResponseDTO;
import com.example.tableviewer.model.Borrow;
import com.example.tableviewer.model.BorrowId;
import com.example.tableviewer.repository.BorrowRepository;
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
class BorrowServiceTest {

    @Mock
    private BorrowRepository repository;

    @Mock
    private InventoryService inventoryService;

    private BorrowService service;

    @BeforeEach
    void setUp() {
        service = new BorrowService(repository, inventoryService);
    }

    @Test
    void findAll_shouldReturnPageOfBorrows() {
        Borrow borrow = createTestBorrow();
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(borrow)));

        Page<BorrowResponseDTO> result = service.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("978-7-111-54742-1", result.getContent().get(0).getIsbn());
    }

    @Test
    void findById_shouldReturnBorrowWhenExists() {
        Borrow borrow = createTestBorrow();
        BorrowId id = new BorrowId("978-7-111-54742-1", 1L, 1L);
        when(repository.findById(id)).thenReturn(Optional.of(borrow));

        Optional<BorrowResponseDTO> result = service.findById("978-7-111-54742-1", 1L, 1L);

        assertTrue(result.isPresent());
        assertEquals("978-7-111-54742-1", result.get().getIsbn());
    }

    @Test
    void findByIsbn_shouldReturnMatchingBorrows() {
        Borrow borrow1 = createTestBorrow();
        Borrow borrow2 = new Borrow();
        borrow2.setId(new BorrowId("978-7-111-54742-1", 2L, 1L));
        borrow2.setUserId(1L);
        borrow2.setStatus("BORROWED");
        when(repository.findByIsbn("978-7-111-54742-1")).thenReturn(Arrays.asList(borrow1, borrow2));

        List<BorrowResponseDTO> result = service.findByIsbn("978-7-111-54742-1");

        assertEquals(2, result.size());
    }

    @Test
    void create_shouldSaveAndReturnBorrow() {
        BorrowRequestDTO dto = new BorrowRequestDTO();
        dto.setIsbn("978-7-111-54742-1");
        dto.setLocationId(1L);
        dto.setReaderId(1L);
        dto.setUserId(1L);
        dto.setBorrowDate(LocalDate.of(2024, 1, 15));
        dto.setDueDate(LocalDate.of(2024, 2, 15));
        dto.setStatus("BORROWED");

        // Mock inventory decrease
        when(inventoryService.decreaseQty(anyString(), anyLong(), anyInt())).thenReturn(true);

        Borrow savedBorrow = createTestBorrow();
        when(repository.save(any(Borrow.class))).thenReturn(savedBorrow);

        BorrowResponseDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals("978-7-111-54742-1", result.getIsbn());
        verify(inventoryService, times(1)).decreaseQty("978-7-111-54742-1", 1L, 1);
        verify(repository, times(1)).save(any(Borrow.class));
    }

    @Test
    void update_shouldUpdateAndReturnBorrowWhenExists() {
        Borrow existingBorrow = createTestBorrow();
        BorrowId id = new BorrowId("978-7-111-54742-1", 1L, 1L);
        BorrowRequestDTO updateDto = new BorrowRequestDTO();
        updateDto.setStatus("RENEWED");
        when(repository.findById(id)).thenReturn(Optional.of(existingBorrow));
        when(repository.save(any(Borrow.class))).thenReturn(existingBorrow);

        Optional<BorrowResponseDTO> result = service.update("978-7-111-54742-1", 1L, 1L, updateDto);

        assertTrue(result.isPresent());
        verify(repository, times(1)).save(any(Borrow.class));
    }

    @Test
    void delete_shouldReturnTrueWhenExists() {
        BorrowId id = new BorrowId("978-7-111-54742-1", 1L, 1L);
        when(repository.existsById(id)).thenReturn(true);

        boolean result = service.delete("978-7-111-54742-1", 1L, 1L);

        assertTrue(result);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void delete_shouldReturnFalseWhenNotExists() {
        BorrowId id = new BorrowId("invalid", 999L, 999L);
        when(repository.existsById(id)).thenReturn(false);

        boolean result = service.delete("invalid", 999L, 999L);

        assertFalse(result);
        verify(repository, never()).deleteById(any());
    }

    private Borrow createTestBorrow() {
        Borrow borrow = new Borrow();
        borrow.setId(new BorrowId("978-7-111-54742-1", 1L, 1L));
        borrow.setUserId(1L);
        borrow.setBorrowDate(LocalDate.of(2024, 1, 15));
        borrow.setDueDate(LocalDate.of(2024, 2, 15));
        borrow.setStatus("BORROWED");
        return borrow;
    }
}
