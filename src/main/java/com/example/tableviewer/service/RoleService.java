package com.example.tableviewer.service;

import com.example.tableviewer.dto.RoleRequestDTO;
import com.example.tableviewer.dto.RoleResponseDTO;
import com.example.tableviewer.mapper.RoleMapper;
import com.example.tableviewer.model.Role;
import com.example.tableviewer.repository.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {

    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<RoleResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(RoleMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<RoleResponseDTO> findById(Long id) {
        return repository.findById(id).map(RoleMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<RoleResponseDTO> findByRoleName(String roleName) {
        return repository.findByRoleNameContaining(roleName)
                .stream()
                .map(RoleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RoleResponseDTO create(RoleRequestDTO dto) {
        Role model = RoleMapper.toModel(dto);
        return RoleMapper.toDTO(repository.save(model));
    }

    public Optional<RoleResponseDTO> update(Long id, RoleRequestDTO dto) {
        return repository.findById(id).map(model -> {
            model.setRoleName(dto.getRoleName());
            model.setDescription(dto.getDescription());
            return RoleMapper.toDTO(repository.save(model));
        });
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
