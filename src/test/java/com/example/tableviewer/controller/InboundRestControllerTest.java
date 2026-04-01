package com.example.tableviewer.controller;

import com.example.tableviewer.dto.InboundResponseDTO;
import com.example.tableviewer.service.InboundService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InboundRestController.class)
class InboundRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InboundService service;

    @Test
    void findAll_shouldReturnEmptyPage() throws Exception {
        mockMvc.perform(get("/api/inbounds"))
                .andExpect(status().isOk());
    }

    @Test
    void findById_shouldReturnInboundWhenExists() throws Exception {
        InboundResponseDTO dto = createTestInboundResponse();
        when(service.findById(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/inbounds/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("978-7-111-54742-1"));
    }

    @Test
    void findById_shouldReturn404WhenNotExists() throws Exception {
        when(service.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/inbounds/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByIsbn_shouldReturnMatchingInbounds() throws Exception {
        InboundResponseDTO dto = createTestInboundResponse();
        when(service.findByIsbn("978-7-111-54742-1")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/inbounds/search").param("isbn", "978-7-111-54742-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isbn").value("978-7-111-54742-1"));
    }

    @Test
    void create_shouldReturnCreatedInbound() throws Exception {
        InboundResponseDTO dto = createTestInboundResponse();
        when(service.create(any())).thenReturn(dto);

        mockMvc.perform(post("/api/inbounds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"isbn\":\"978-7-111-54742-1\",\"userId\":1,\"qty\":100}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn").value("978-7-111-54742-1"));
    }

    @Test
    void update_shouldReturnUpdatedInboundWhenExists() throws Exception {
        InboundResponseDTO dto = createTestInboundResponse();
        when(service.update(anyLong(), any())).thenReturn(Optional.of(dto));

        mockMvc.perform(put("/api/inbounds/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"isbn\":\"978-7-111-54742-1\",\"userId\":1,\"qty\":150}"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_shouldReturn204WhenExists() throws Exception {
        when(service.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/inbounds/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_shouldReturn404WhenNotExists() throws Exception {
        when(service.delete(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/inbounds/999"))
                .andExpect(status().isNotFound());
    }

    private InboundResponseDTO createTestInboundResponse() {
        InboundResponseDTO dto = new InboundResponseDTO();
        dto.setId(1L);
        dto.setIsbn("978-7-111-54742-1");
        dto.setUserId(1L);
        dto.setLocationId(1L);
        dto.setQty(100);
        dto.setReceivedDate(LocalDate.of(2024, 1, 15));
        dto.setPutawayDate(LocalDateTime.of(2024, 1, 16, 10, 30));
        return dto;
    }
}
