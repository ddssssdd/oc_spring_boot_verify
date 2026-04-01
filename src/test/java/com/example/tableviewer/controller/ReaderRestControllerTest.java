package com.example.tableviewer.controller;

import com.example.tableviewer.dto.ReaderResponseDTO;
import com.example.tableviewer.service.ReaderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReaderRestController.class)
class ReaderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReaderService service;

    @Test
    void findAll_shouldReturnEmptyPage() throws Exception {
        mockMvc.perform(get("/api/readers"))
                .andExpect(status().isOk());
    }

    @Test
    void findById_shouldReturnReaderWhenExists() throws Exception {
        ReaderResponseDTO dto = createTestReaderResponse();
        when(service.findById(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/readers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("张三"));
    }

    @Test
    void findById_shouldReturn404WhenNotExists() throws Exception {
        when(service.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/readers/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByName_shouldReturnMatchingReaders() throws Exception {
        ReaderResponseDTO dto = createTestReaderResponse();
        when(service.findByName("张三")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/readers/search").param("name", "张三"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("张三"));
    }

    @Test
    void create_shouldReturnCreatedReader() throws Exception {
        ReaderResponseDTO dto = createTestReaderResponse();
        when(service.create(any())).thenReturn(dto);

        mockMvc.perform(post("/api/readers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"张三\",\"gender\":\"男\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("张三"));
    }

    @Test
    void update_shouldReturnUpdatedReaderWhenExists() throws Exception {
        ReaderResponseDTO dto = createTestReaderResponse();
        when(service.update(anyLong(), any())).thenReturn(Optional.of(dto));

        mockMvc.perform(put("/api/readers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"张三\",\"gender\":\"男\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_shouldReturn204WhenExists() throws Exception {
        when(service.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/readers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_shouldReturn404WhenNotExists() throws Exception {
        when(service.delete(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/readers/999"))
                .andExpect(status().isNotFound());
    }

    private ReaderResponseDTO createTestReaderResponse() {
        ReaderResponseDTO dto = new ReaderResponseDTO();
        dto.setId(1L);
        dto.setName("张三");
        dto.setGender("男");
        dto.setBirthday(LocalDate.of(1990, 5, 15));
        dto.setRegisteredDate(LocalDate.of(2023, 1, 10));
        dto.setActiveFlag(true);
        dto.setStatus("ACTIVE");
        return dto;
    }
}
