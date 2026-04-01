package com.example.tableviewer.controller;

import com.example.tableviewer.dto.InventoryRequestDTO;
import com.example.tableviewer.dto.InventoryResponseDTO;
import com.example.tableviewer.service.InventoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
@CrossOrigin(origins = "*")
public class InventoryRestController {

    private final InventoryService service;

    public InventoryRestController(InventoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<InventoryResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "isbn") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return ResponseEntity.ok(service.findAll(PageRequest.of(page, size, Sort.by(dir, sort))));
    }

    @GetMapping("/{isbn}/{locationId}")
    public ResponseEntity<InventoryResponseDTO> findById(
            @PathVariable String isbn,
            @PathVariable Long locationId) {
        return service.findById(isbn, locationId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<InventoryResponseDTO>> findByIsbn(@RequestParam String isbn) {
        return ResponseEntity.ok(service.findByIsbn(isbn));
    }

    @PostMapping
    public ResponseEntity<InventoryResponseDTO> create(@RequestBody InventoryRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{isbn}/{locationId}")
    public ResponseEntity<InventoryResponseDTO> update(
            @PathVariable String isbn,
            @PathVariable Long locationId,
            @RequestBody InventoryRequestDTO dto) {
        return service.update(isbn, locationId, dto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{isbn}/{locationId}")
    public ResponseEntity<Void> delete(
            @PathVariable String isbn,
            @PathVariable Long locationId) {
        if (service.delete(isbn, locationId)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
