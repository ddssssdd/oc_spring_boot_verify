package com.example.tableviewer.service;

import com.example.tableviewer.dto.InboundRequestDTO;
import com.example.tableviewer.dto.InboundResponseDTO;
import com.example.tableviewer.mapper.InboundMapper;
import com.example.tableviewer.model.Inbound;
import com.example.tableviewer.repository.InboundRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class InboundService {

    private final InboundRepository repository;

    public InboundService(InboundRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<InboundResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(InboundMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<InboundResponseDTO> findById(Long id) {
        return repository.findById(id).map(InboundMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<InboundResponseDTO> findByIsbn(String isbn) {
        return repository.findByIsbn(isbn)
                .stream()
                .map(InboundMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<InboundResponseDTO> search(String isbn, Long locationId, Pageable pageable) {
        return repository.search(isbn, locationId, pageable).map(InboundMapper::toDTO);
    }

    public InboundResponseDTO create(InboundRequestDTO dto) {
        Inbound model = InboundMapper.toModel(dto);
        return InboundMapper.toDTO(repository.save(model));
    }

    public Optional<InboundResponseDTO> update(Long id, InboundRequestDTO dto) {
        return repository.findById(id).map(model -> {
            model.setIsbn(dto.getIsbn());
            model.setUserId(dto.getUserId());
            model.setLocationId(dto.getLocationId());
            model.setQty(dto.getQty());
            model.setReceivedDate(dto.getReceivedDate());
            model.setPutawayDate(dto.getPutawayDate());
            return InboundMapper.toDTO(repository.save(model));
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
