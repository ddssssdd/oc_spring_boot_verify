package com.example.tableviewer.service;

import com.example.tableviewer.dto.InboundRequestDTO;
import com.example.tableviewer.dto.InboundResponseDTO;
import com.example.tableviewer.model.Inbound;
import com.example.tableviewer.repository.InboundRepository;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InboundServiceTest {

    @Mock
    private InboundRepository repository;

    private InboundService service;

    @BeforeEach
    void setUp() {
        service = new InboundService(repository);
    }

    @Test
    void findAll_shouldReturnPageOfInbounds() {
        Inbound inbound = createTestInbound();
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(inbound)));

        Page<InboundResponseDTO> result = service.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("978-7-111-54742-1", result.getContent().get(0).getIsbn());
    }

    @Test
    void findById_shouldReturnInboundWhenExists() {
        Inbound inbound = createTestInbound();
        when(repository.findById(1L)).thenReturn(Optional.of(inbound));

        Optional<InboundResponseDTO> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("978-7-111-54742-1", result.get().getIsbn());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotExists() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        Optional<InboundResponseDTO> result = service.findById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void findByIsbn_shouldReturnMatchingInbounds() {
        Inbound inbound1 = createTestInbound();
        Inbound inbound2 = new Inbound();
        inbound2.setId(2L);
        inbound2.setIsbn("978-7-111-54742-1");
        inbound2.setUserId(1L);
        inbound2.setQty(50);
        when(repository.findByIsbn("978-7-111-54742-1")).thenReturn(Arrays.asList(inbound1, inbound2));

        List<InboundResponseDTO> result = service.findByIsbn("978-7-111-54742-1");

        assertEquals(2, result.size());
    }

    @Test
    void create_shouldSaveAndReturnInbound() {
        InboundRequestDTO dto = new InboundRequestDTO();
        dto.setIsbn("978-7-111-54742-1");
        dto.setUserId(1L);
        dto.setLocationId(1L);
        dto.setQty(100);
        dto.setReceivedDate(LocalDate.of(2024, 1, 15));
        dto.setPutawayDate(LocalDateTime.of(2024, 1, 16, 10, 30));

        Inbound savedInbound = new Inbound();
        savedInbound.setId(1L);
        savedInbound.setIsbn(dto.getIsbn());
        savedInbound.setUserId(dto.getUserId());
        savedInbound.setLocationId(dto.getLocationId());
        savedInbound.setQty(dto.getQty());
        savedInbound.setReceivedDate(dto.getReceivedDate());
        savedInbound.setPutawayDate(dto.getPutawayDate());
        when(repository.save(any(Inbound.class))).thenReturn(savedInbound);

        InboundResponseDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals("978-7-111-54742-1", result.getIsbn());
        verify(repository, times(1)).save(any(Inbound.class));
    }

    @Test
    void update_shouldUpdateAndReturnInboundWhenExists() {
        Inbound existingInbound = createTestInbound();
        InboundRequestDTO updateDto = new InboundRequestDTO();
        updateDto.setIsbn("978-7-111-54742-1");
        updateDto.setUserId(1L);
        updateDto.setLocationId(2L);
        updateDto.setQty(150);
        when(repository.findById(1L)).thenReturn(Optional.of(existingInbound));
        when(repository.save(any(Inbound.class))).thenReturn(existingInbound);

        Optional<InboundResponseDTO> result = service.update(1L, updateDto);

        assertTrue(result.isPresent());
        verify(repository, times(1)).save(any(Inbound.class));
    }

    @Test
    void update_shouldReturnEmptyWhenNotExists() {
        InboundRequestDTO updateDto = new InboundRequestDTO();
        when(repository.findById(999L)).thenReturn(Optional.empty());

        Optional<InboundResponseDTO> result = service.update(999L, updateDto);

        assertFalse(result.isPresent());
        verify(repository, never()).save(any(Inbound.class));
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

    private Inbound createTestInbound() {
        Inbound inbound = new Inbound();
        inbound.setId(1L);
        inbound.setIsbn("978-7-111-54742-1");
        inbound.setUserId(1L);
        inbound.setLocationId(1L);
        inbound.setQty(100);
        inbound.setReceivedDate(LocalDate.of(2024, 1, 15));
        inbound.setPutawayDate(LocalDateTime.of(2024, 1, 16, 10, 30));
        return inbound;
    }
}
