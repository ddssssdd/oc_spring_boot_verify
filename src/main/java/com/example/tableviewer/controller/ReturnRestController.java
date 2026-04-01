package com.example.tableviewer.controller;

import com.example.tableviewer.dto.ReturnRequestDTO;
import com.example.tableviewer.dto.ReturnResponseDTO;
import com.example.tableviewer.service.ReturnService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/returns")
@CrossOrigin(origins = "*")
public class ReturnRestController {

    private final ReturnService service;

    public ReturnRestController(ReturnService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ReturnResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return ResponseEntity.ok(service.findAll(PageRequest.of(page, size, Sort.by(dir, sort))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnResponseDTO> findById(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReturnResponseDTO>> findByIsbn(@RequestParam String isbn) {
        return ResponseEntity.ok(service.findByIsbn(isbn));
    }

    @PostMapping
    public ResponseEntity<ReturnResponseDTO> create(@RequestBody ReturnRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReturnResponseDTO> update(@PathVariable Long id, @RequestBody ReturnRequestDTO dto) {
        return service.update(id, dto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
