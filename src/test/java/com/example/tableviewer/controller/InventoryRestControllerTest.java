package com.example.tableviewer.controller;

import com.example.tableviewer.dto.InventoryResponseDTO;
import com.example.tableviewer.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryRestController.class)
class InventoryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService service;

    @Test
    void findAll_shouldReturnEmptyPage() throws Exception {
        mockMvc.perform(get("/api/inventories"))
                .andExpect(status().isOk());
    }

    @Test
    void findById_shouldReturnInventoryWhenExists() throws Exception {
        InventoryResponseDTO dto = createTestInventoryResponse();
        when(service.findById("978-7-111-54742-1", 1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/inventories/978-7-111-54742-1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("978-7-111-54742-1"));
    }

    @Test
    void findById_shouldReturn404WhenNotExists() throws Exception {
        when(service.findById("invalid", 999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/inventories/invalid/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByIsbn_shouldReturnMatchingInventories() throws Exception {
        InventoryResponseDTO dto = createTestInventoryResponse();
        when(service.findByIsbn("978-7-111-54742-1")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/inventories/search").param("isbn", "978-7-111-54742-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isbn").value("978-7-111-54742-1"));
    }

    @Test
    void create_shouldReturnCreatedInventory() throws Exception {
        InventoryResponseDTO dto = createTestInventoryResponse();
        when(service.create(any())).thenReturn(dto);

        mockMvc.perform(post("/api/inventories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"isbn\":\"978-7-111-54742-1\",\"locationId\":1,\"qty\":100}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn").value("978-7-111-54742-1"));
    }

    @Test
    void update_shouldReturnUpdatedInventoryWhenExists() throws Exception {
        InventoryResponseDTO dto = createTestInventoryResponse();
        when(service.update(eq("978-7-111-54742-1"), eq(1L), any())).thenReturn(Optional.of(dto));

        mockMvc.perform(put("/api/inventories/978-7-111-54742-1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"isbn\":\"978-7-111-54742-1\",\"locationId\":1,\"qty\":150}"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_shouldReturn204WhenExists() throws Exception {
        when(service.delete("978-7-111-54742-1", 1L)).thenReturn(true);

        mockMvc.perform(delete("/api/inventories/978-7-111-54742-1/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_shouldReturn404WhenNotExists() throws Exception {
        when(service.delete("invalid", 999L)).thenReturn(false);

        mockMvc.perform(delete("/api/inventories/invalid/999"))
                .andExpect(status().isNotFound());
    }

    private InventoryResponseDTO createTestInventoryResponse() {
        InventoryResponseDTO dto = new InventoryResponseDTO();
        dto.setIsbn("978-7-111-54742-1");
        dto.setLocationId(1L);
        dto.setQty(100);
        dto.setUpdateDate(LocalDateTime.of(2024, 1, 15, 10, 30));
        return dto;
    }
}
