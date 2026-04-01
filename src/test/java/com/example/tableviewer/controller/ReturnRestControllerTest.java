package com.example.tableviewer.controller;

import com.example.tableviewer.dto.ReturnResponseDTO;
import com.example.tableviewer.service.ReturnService;
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

@WebMvcTest(ReturnRestController.class)
class ReturnRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReturnService service;

    @Test
    void findAll_shouldReturnEmptyPage() throws Exception {
        mockMvc.perform(get("/api/returns"))
                .andExpect(status().isOk());
    }

    @Test
    void findById_shouldReturnReturnWhenExists() throws Exception {
        ReturnResponseDTO dto = createTestReturnResponse();
        when(service.findById(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/returns/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("978-7-111-54742-1"));
    }

    @Test
    void findById_shouldReturn404WhenNotExists() throws Exception {
        when(service.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/returns/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByIsbn_shouldReturnMatchingReturns() throws Exception {
        ReturnResponseDTO dto = createTestReturnResponse();
        when(service.findByIsbn("978-7-111-54742-1")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/returns/search").param("isbn", "978-7-111-54742-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isbn").value("978-7-111-54742-1"));
    }

    @Test
    void create_shouldReturnCreatedReturn() throws Exception {
        ReturnResponseDTO dto = createTestReturnResponse();
        when(service.create(any())).thenReturn(dto);

        mockMvc.perform(post("/api/returns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"isbn\":\"978-7-111-54742-1\",\"readerId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn").value("978-7-111-54742-1"));
    }

    @Test
    void update_shouldReturnUpdatedReturnWhenExists() throws Exception {
        ReturnResponseDTO dto = createTestReturnResponse();
        when(service.update(anyLong(), any())).thenReturn(Optional.of(dto));

        mockMvc.perform(put("/api/returns/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"isbn\":\"978-7-111-54742-1\",\"readerId\":1}"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_shouldReturn204WhenExists() throws Exception {
        when(service.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/returns/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_shouldReturn404WhenNotExists() throws Exception {
        when(service.delete(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/returns/999"))
                .andExpect(status().isNotFound());
    }

    private ReturnResponseDTO createTestReturnResponse() {
        ReturnResponseDTO dto = new ReturnResponseDTO();
        dto.setId(1L);
        dto.setIsbn("978-7-111-54742-1");
        dto.setReaderId(1L);
        dto.setReturnDate(LocalDate.of(2024, 2, 15));
        dto.setLocationId(1L);
        dto.setUserId(1L);
        return dto;
    }
}
