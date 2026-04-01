package com.example.tableviewer.mapper;

import com.example.tableviewer.dto.RoleRequestDTO;
import com.example.tableviewer.dto.RoleResponseDTO;
import com.example.tableviewer.model.Role;

public class RoleMapper {

    private RoleMapper() {}

    public static RoleResponseDTO toDTO(Role role) {
        if (role == null) return null;
        RoleResponseDTO dto = new RoleResponseDTO();
        dto.setId(role.getId());
        dto.setRoleName(role.getRoleName());
        dto.setDescription(role.getDescription());
        return dto;
    }

    public static Role toModel(RoleRequestDTO dto) {
        if (dto == null) return null;
        Role role = new Role();
        role.setId(dto.getId());
        role.setRoleName(dto.getRoleName());
        role.setDescription(dto.getDescription());
        return role;
    }
}
