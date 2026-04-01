package com.example.tableviewer.dto;

import com.example.tableviewer.model.Role;

public class RoleResponseDTO {

    private Long id;
    private String roleName;
    private String description;

    public static RoleResponseDTO from(Role role) {
        if (role == null) return null;
        RoleResponseDTO dto = new RoleResponseDTO();
        dto.setId(role.getId());
        dto.setRoleName(role.getRoleName());
        dto.setDescription(role.getDescription());
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
