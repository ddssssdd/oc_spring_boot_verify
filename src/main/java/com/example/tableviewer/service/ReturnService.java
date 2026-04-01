package com.example.tableviewer.service;

import com.example.tableviewer.dto.ReturnRequestDTO;
import com.example.tableviewer.dto.ReturnResponseDTO;
import com.example.tableviewer.mapper.ReturnMapper;
import com.example.tableviewer.model.Return;
import com.example.tableviewer.repository.ReturnRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReturnService {

    private final ReturnRepository repository;
    private final InventoryService inventoryService;

    public ReturnService(ReturnRepository repository, InventoryService inventoryService) {
        this.repository = repository;
        this.inventoryService = inventoryService;
    }

    @Transactional(readOnly = true)
    public Page<ReturnResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(ReturnMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<ReturnResponseDTO> findById(Long id) {
        return repository.findById(id).map(ReturnMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<ReturnResponseDTO> findByIsbn(String isbn) {
        return repository.findByIsbn(isbn)
                .stream()
                .map(ReturnMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ReturnResponseDTO create(ReturnRequestDTO dto) {
        // Increase inventory stock on return
        if (dto.getLocationId() != null) {
            inventoryService.increaseQty(dto.getIsbn(), dto.getLocationId(), 1);
        }
        Return model = ReturnMapper.toModel(dto);
        return ReturnMapper.toDTO(repository.save(model));
    }

    public Optional<ReturnResponseDTO> update(Long id, ReturnRequestDTO dto) {
        return repository.findById(id).map(model -> {
            model.setIsbn(dto.getIsbn());
            model.setReaderId(dto.getReaderId());
            model.setReturnDate(dto.getReturnDate());
            model.setLocationId(dto.getLocationId());
            model.setUserId(dto.getUserId());
            return ReturnMapper.toDTO(repository.save(model));
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
