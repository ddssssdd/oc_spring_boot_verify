package com.example.tableviewer.service;

import com.example.tableviewer.dto.ReaderRequestDTO;
import com.example.tableviewer.dto.ReaderResponseDTO;
import com.example.tableviewer.mapper.ReaderMapper;
import com.example.tableviewer.model.Reader;
import com.example.tableviewer.repository.ReaderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReaderService {

    private final ReaderRepository repository;

    public ReaderService(ReaderRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<ReaderResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(ReaderMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<ReaderResponseDTO> findById(Long id) {
        return repository.findById(id).map(ReaderMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<ReaderResponseDTO> findByName(String name) {
        return repository.findByNameContaining(name)
                .stream()
                .map(ReaderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ReaderResponseDTO create(ReaderRequestDTO dto) {
        Reader model = ReaderMapper.toModel(dto);
        return ReaderMapper.toDTO(repository.save(model));
    }

    public Optional<ReaderResponseDTO> update(Long id, ReaderRequestDTO dto) {
        return repository.findById(id).map(model -> {
            model.setName(dto.getName());
            model.setGender(dto.getGender());
            model.setBirthday(dto.getBirthday());
            model.setRegisteredDate(dto.getRegisteredDate());
            model.setActiveFlag(dto.getActiveFlag());
            model.setStatus(dto.getStatus());
            return ReaderMapper.toDTO(repository.save(model));
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
