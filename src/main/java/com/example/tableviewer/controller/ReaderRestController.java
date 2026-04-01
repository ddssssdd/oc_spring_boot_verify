package com.example.tableviewer.controller;

import com.example.tableviewer.dto.ReaderRequestDTO;
import com.example.tableviewer.dto.ReaderResponseDTO;
import com.example.tableviewer.service.ReaderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readers")
@CrossOrigin(origins = "*")
public class ReaderRestController {

    private final ReaderService service;

    public ReaderRestController(ReaderService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ReaderResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return ResponseEntity.ok(service.findAll(PageRequest.of(page, size, Sort.by(dir, sort))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReaderResponseDTO> findById(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReaderResponseDTO>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(service.findByName(name));
    }

    @PostMapping
    public ResponseEntity<ReaderResponseDTO> create(@RequestBody ReaderRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReaderResponseDTO> update(@PathVariable Long id, @RequestBody ReaderRequestDTO dto) {
        return service.update(id, dto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
