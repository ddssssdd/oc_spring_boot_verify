package com.example.tableviewer.controller;

import com.example.tableviewer.dto.BorrowResponseDTO;
import com.example.tableviewer.service.BorrowService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BorrowRestController.class)
class BorrowRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowService service;

    @Test
    void findAll_shouldReturnEmptyPage() throws Exception {
        mockMvc.perform(get("/api/borrows"))
                .andExpect(status().isOk());
    }

    @Test
    void findById_shouldReturnBorrowWhenExists() throws Exception {
        BorrowResponseDTO dto = createTestBorrowResponse();
        when(service.findById("978-7-111-54742-1", 1L, 1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/borrows/978-7-111-54742-1/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("978-7-111-54742-1"));
    }

    @Test
    void findById_shouldReturn404WhenNotExists() throws Exception {
        when(service.findById("invalid", 999L, 999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/borrows/invalid/999/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByIsbn_shouldReturnMatchingBorrows() throws Exception {
        BorrowResponseDTO dto = createTestBorrowResponse();
        when(service.findByIsbn("978-7-111-54742-1")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/borrows/search").param("isbn", "978-7-111-54742-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isbn").value("978-7-111-54742-1"));
    }

    @Test
    void create_shouldReturnCreatedBorrow() throws Exception {
        BorrowResponseDTO dto = createTestBorrowResponse();
        when(service.create(any())).thenReturn(dto);

        mockMvc.perform(post("/api/borrows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"isbn\":\"978-7-111-54742-1\",\"locationId\":1,\"readerId\":1,\"userId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn").value("978-7-111-54742-1"));
    }

    @Test
    void update_shouldReturnUpdatedBorrowWhenExists() throws Exception {
        BorrowResponseDTO dto = createTestBorrowResponse();
        when(service.update(eq("978-7-111-54742-1"), eq(1L), eq(1L), any())).thenReturn(Optional.of(dto));

        mockMvc.perform(put("/api/borrows/978-7-111-54742-1/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"isbn\":\"978-7-111-54742-1\",\"locationId\":1,\"readerId\":1,\"userId\":1}"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_shouldReturn204WhenExists() throws Exception {
        when(service.delete("978-7-111-54742-1", 1L, 1L)).thenReturn(true);

        mockMvc.perform(delete("/api/borrows/978-7-111-54742-1/1/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_shouldReturn404WhenNotExists() throws Exception {
        when(service.delete("invalid", 999L, 999L)).thenReturn(false);

        mockMvc.perform(delete("/api/borrows/invalid/999/999"))
                .andExpect(status().isNotFound());
    }

    private BorrowResponseDTO createTestBorrowResponse() {
        BorrowResponseDTO dto = new BorrowResponseDTO();
        dto.setIsbn("978-7-111-54742-1");
        dto.setLocationId(1L);
        dto.setReaderId(1L);
        dto.setUserId(1L);
        dto.setBorrowDate(LocalDate.of(2024, 1, 15));
        dto.setDueDate(LocalDate.of(2024, 2, 15));
        dto.setStatus("BORROWED");
        return dto;
    }
}
