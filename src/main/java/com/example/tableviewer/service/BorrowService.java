package com.example.tableviewer.service;

import com.example.tableviewer.dto.BorrowRequestDTO;
import com.example.tableviewer.dto.BorrowResponseDTO;
import com.example.tableviewer.mapper.BorrowMapper;
import com.example.tableviewer.model.Borrow;
import com.example.tableviewer.model.BorrowId;
import com.example.tableviewer.repository.BorrowRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BorrowService {

    private final BorrowRepository repository;

    public BorrowService(BorrowRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<BorrowResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(BorrowMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<BorrowResponseDTO> findById(String isbn, Long locationId, Long readerId) {
        return repository.findById(new BorrowId(isbn, locationId, readerId)).map(BorrowMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<BorrowResponseDTO> findByIsbn(String isbn) {
        return repository.findByIsbn(isbn)
                .stream()
                .map(BorrowMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BorrowResponseDTO create(BorrowRequestDTO dto) {
        Borrow model = BorrowMapper.toModel(dto);
        return BorrowMapper.toDTO(repository.save(model));
    }

    public Optional<BorrowResponseDTO> update(String isbn, Long locationId, Long readerId, BorrowRequestDTO dto) {
        return repository.findById(new BorrowId(isbn, locationId, readerId)).map(model -> {
            model.setUserId(dto.getUserId());
            model.setBorrowDate(dto.getBorrowDate());
            model.setDueDate(dto.getDueDate());
            model.setRenewDate(dto.getRenewDate());
            model.setStatus(dto.getStatus());
            return BorrowMapper.toDTO(repository.save(model));
        });
    }

    public boolean delete(String isbn, Long locationId, Long readerId) {
        BorrowId id = new BorrowId(isbn, locationId, readerId);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
