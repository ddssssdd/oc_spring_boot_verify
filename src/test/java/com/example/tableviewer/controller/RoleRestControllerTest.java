package com.example.tableviewer.controller;

import com.example.tableviewer.dto.RoleResponseDTO;
import com.example.tableviewer.service.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoleRestController.class)
class RoleRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService service;

    @Test
    void findAll_shouldReturnEmptyPage() throws Exception {
        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk());
    }

    @Test
    void findById_shouldReturnRoleWhenExists() throws Exception {
        RoleResponseDTO dto = createTestRoleResponse();
        when(service.findById(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleName").value("管理员"));
    }

    @Test
    void findById_shouldReturn404WhenNotExists() throws Exception {
        when(service.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/roles/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByRoleName_shouldReturnMatchingRoles() throws Exception {
        RoleResponseDTO dto = createTestRoleResponse();
        when(service.findByRoleName("管理员")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/roles/search").param("roleName", "管理员"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roleName").value("管理员"));
    }

    @Test
    void create_shouldReturnCreatedRole() throws Exception {
        RoleResponseDTO dto = createTestRoleResponse();
        when(service.create(any())).thenReturn(dto);

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roleName\":\"管理员\",\"description\":\"系统管理员\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roleName").value("管理员"));
    }

    @Test
    void update_shouldReturnUpdatedRoleWhenExists() throws Exception {
        RoleResponseDTO dto = createTestRoleResponse();
        when(service.update(anyLong(), any())).thenReturn(Optional.of(dto));

        mockMvc.perform(put("/api/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roleName\":\"超级管理员\",\"description\":\"更新后描述\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_shouldReturn204WhenExists() throws Exception {
        when(service.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/roles/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_shouldReturn404WhenNotExists() throws Exception {
        when(service.delete(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/roles/999"))
                .andExpect(status().isNotFound());
    }

    private RoleResponseDTO createTestRoleResponse() {
        RoleResponseDTO dto = new RoleResponseDTO();
        dto.setId(1L);
        dto.setRoleName("管理员");
        dto.setDescription("系统管理员");
        return dto;
    }
}
