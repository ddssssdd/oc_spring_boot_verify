package com.example.tableviewer.service;

import com.example.tableviewer.dto.RoleRequestDTO;
import com.example.tableviewer.dto.RoleResponseDTO;
import com.example.tableviewer.model.Role;
import com.example.tableviewer.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository repository;

    private RoleService service;

    @BeforeEach
    void setUp() {
        service = new RoleService(repository);
    }

    @Test
    void findAll_shouldReturnPageOfRoles() {
        Role role = createTestRole();
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(role)));

        Page<RoleResponseDTO> result = service.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("管理员", result.getContent().get(0).getRoleName());
    }

    @Test
    void findById_shouldReturnRoleWhenExists() {
        Role role = createTestRole();
        when(repository.findById(1L)).thenReturn(Optional.of(role));

        Optional<RoleResponseDTO> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("管理员", result.get().getRoleName());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotExists() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        Optional<RoleResponseDTO> result = service.findById(999L);

        assertFalse(result.isPresent());
    }

    @Test
    void findByRoleName_shouldReturnMatchingRoles() {
        Role role1 = createTestRole();
        Role role2 = new Role();
        role2.setId(2L);
        role2.setRoleName("超级管理员");
        when(repository.findByRoleNameContaining("管理员")).thenReturn(Arrays.asList(role1, role2));

        List<RoleResponseDTO> result = service.findByRoleName("管理员");

        assertEquals(2, result.size());
    }

    @Test
    void create_shouldSaveAndReturnRole() {
        RoleRequestDTO dto = new RoleRequestDTO();
        dto.setRoleName("新角色");
        dto.setDescription("新角色描述");

        Role savedRole = createTestRole();
        when(repository.save(any(Role.class))).thenReturn(savedRole);

        RoleResponseDTO result = service.create(dto);

        assertNotNull(result);
        assertEquals("管理员", result.getRoleName());
        verify(repository, times(1)).save(any(Role.class));
    }

    @Test
    void update_shouldUpdateAndReturnRoleWhenExists() {
        Role existingRole = createTestRole();
        RoleRequestDTO updateDto = new RoleRequestDTO();
        updateDto.setRoleName("超级管理员");
        updateDto.setDescription("更新后描述");
        when(repository.findById(1L)).thenReturn(Optional.of(existingRole));
        when(repository.save(any(Role.class))).thenReturn(existingRole);

        Optional<RoleResponseDTO> result = service.update(1L, updateDto);

        assertTrue(result.isPresent());
        verify(repository, times(1)).save(any(Role.class));
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

    private Role createTestRole() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName("管理员");
        role.setDescription("系统管理员");
        return role;
    }
}
