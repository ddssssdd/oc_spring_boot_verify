package com.example.tableviewer.service;

import com.example.tableviewer.dto.ReaderRequestDTO;
import com.example.tableviewer.dto.ReaderResponseDTO;
import com.example.tableviewer.mapper.ReaderMapper;
import com.example.tableviewer.model.Reader;
import com.example.tableviewer.repository.ReaderRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReaderServiceTest {

    @Mock
    private ReaderRepository repository;

    private ReaderService service;

    @BeforeEach
    void setUp() {
        service = new ReaderService(repository);
    }

    @Test
    void findAll_shouldReturnPageOfReaders() {
        Reader reader = createTestReader();
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(reader)));

        Page<ReaderResponseDTO> result = service.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("张三", result.getContent().get(0).getName());
    }

    @Test
    void findById_shouldReturnReaderWhenExists() {
        Reader reader = createTestReader();
        when(repository.findById(1L)).thenReturn(Optional.of(reader));

        Optional<ReaderResponseDTO> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("张三", result.get().getName());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotExists() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        Optional<ReaderResponseDTO> result = service.findById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void findByName_shouldReturnMatchingReaders() {
        Reader reader1 = createTestReader();
        Reader reader2 = new Reader();
        reader2.setId(2L);
        reader2.setName("张三丰");
        when(repository.findByNameContaining("张三")).thenReturn(Arrays.asList(reader1, reader2));

        List<ReaderResponseDTO> result = service.findByName("张三");

        assertEquals(2, result.size());
    }

    @Test
    void create_shouldSaveAndReturnReader() {
        ReaderRequestDTO dto = new ReaderRequestDTO();
        dto.setName("新读者");
        dto.setGender("女");
        dto.setBirthday(LocalDate.of(1992, 3, 10));
        dto.setRegisteredDate(LocalDate.now());
        dto.setActiveFlag(true);
        dto.setStatus("ACTIVE");

        Reader savedReader = ReaderMapper.toModel(dto);
        savedReader.setId(1L);
        when(repository.save(any(Reader.class))).thenReturn(savedReader);

        ReaderResponseDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals("新读者", result.getName());
        verify(repository, times(1)).save(any(Reader.class));
    }

    @Test
    void update_shouldUpdateAndReturnReaderWhenExists() {
        Reader existingReader = createTestReader();
        ReaderRequestDTO updateDto = new ReaderRequestDTO();
        updateDto.setName("更新后姓名");
        updateDto.setGender("女");
        when(repository.findById(1L)).thenReturn(Optional.of(existingReader));
        when(repository.save(any(Reader.class))).thenReturn(existingReader);

        Optional<ReaderResponseDTO> result = service.update(1L, updateDto);

        assertTrue(result.isPresent());
        verify(repository, times(1)).save(any(Reader.class));
    }

    @Test
    void update_shouldReturnEmptyWhenNotExists() {
        ReaderRequestDTO updateDto = new ReaderRequestDTO();
        when(repository.findById(999L)).thenReturn(Optional.empty());

        Optional<ReaderResponseDTO> result = service.update(999L, updateDto);

        assertFalse(result.isPresent());
        verify(repository, never()).save(any(Reader.class));
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

    private Reader createTestReader() {
        Reader reader = new Reader();
        reader.setId(1L);
        reader.setName("张三");
        reader.setGender("男");
        reader.setBirthday(LocalDate.of(1990, 5, 15));
        reader.setRegisteredDate(LocalDate.of(2023, 1, 10));
        reader.setActiveFlag(true);
        reader.setStatus("ACTIVE");
        return reader;
    }
}
