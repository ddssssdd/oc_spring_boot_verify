package com.example.tableviewer.service;

import com.example.tableviewer.dto.InventoryRequestDTO;
import com.example.tableviewer.dto.InventoryResponseDTO;
import com.example.tableviewer.mapper.InventoryMapper;
import com.example.tableviewer.model.Inventory;
import com.example.tableviewer.model.InventoryId;
import com.example.tableviewer.repository.InventoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryService {

    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<InventoryResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(InventoryMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<InventoryResponseDTO> findById(String isbn, Long locationId) {
        return repository.findById(new InventoryId(isbn, locationId)).map(InventoryMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<InventoryResponseDTO> findByIsbn(String isbn) {
        return repository.findById_Isbn(isbn)
                .stream()
                .map(InventoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public InventoryResponseDTO create(InventoryRequestDTO dto) {
        Inventory model = InventoryMapper.toModel(dto);
        return InventoryMapper.toDTO(repository.save(model));
    }

    public Optional<InventoryResponseDTO> update(String isbn, Long locationId, InventoryRequestDTO dto) {
        return repository.findById(new InventoryId(isbn, locationId)).map(model -> {
            model.setQty(dto.getQty());
            model.setUpdateDate(dto.getUpdateDate());
            return InventoryMapper.toDTO(repository.save(model));
        });
    }

    public boolean delete(String isbn, Long locationId) {
        InventoryId id = new InventoryId(isbn, locationId);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Decrease inventory qty by given amount
     * @return true if successful, false if insufficient stock
     */
    public boolean decreaseQty(String isbn, Long locationId, int amount) {
        Optional<Inventory> opt = repository.findById(new InventoryId(isbn, locationId));
        if (opt.isEmpty()) return false;
        Inventory inv = opt.get();
        if (inv.getQty() < amount) return false;
        inv.setQty(inv.getQty() - amount);
        inv.setUpdateDate(java.time.LocalDateTime.now());
        repository.save(inv);
        return true;
    }

    /**
     * Increase inventory qty by given amount
     * @return true if successful, false if record not found
     */
    public boolean increaseQty(String isbn, Long locationId, int amount) {
        Optional<Inventory> opt = repository.findById(new InventoryId(isbn, locationId));
        if (opt.isEmpty()) return false;
        Inventory inv = opt.get();
        inv.setQty(inv.getQty() + amount);
        inv.setUpdateDate(java.time.LocalDateTime.now());
        repository.save(inv);
        return true;
    }

    /**
     * Get current qty for isbn+locationId
     */
    @Transactional(readOnly = true)
    public int getQty(String isbn, Long locationId) {
        return repository.findById(new InventoryId(isbn, locationId))
                .map(Inventory::getQty).orElse(0);
    }
}
