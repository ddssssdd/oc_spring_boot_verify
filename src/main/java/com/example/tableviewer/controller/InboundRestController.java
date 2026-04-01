package com.example.tableviewer.controller;

import com.example.tableviewer.dto.InboundRequestDTO;
import com.example.tableviewer.dto.InboundResponseDTO;
import com.example.tableviewer.service.InboundService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inbounds")
@CrossOrigin(origins = "*")
public class InboundRestController {

    private final InboundService service;

    public InboundRestController(InboundService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<InboundResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return ResponseEntity.ok(service.findAll(PageRequest.of(page, size, Sort.by(dir, sort))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InboundResponseDTO> findById(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<InboundResponseDTO>> findByIsbn(@RequestParam String isbn) {
        return ResponseEntity.ok(service.findByIsbn(isbn));
    }

    // GET /api/inbounds/search/page?isbn=&locationId=&page=0&size=10
    @GetMapping("/search/page")
    public ResponseEntity<Page<InboundResponseDTO>> searchPage(
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) Long locationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return ResponseEntity.ok(service.search(isbn, locationId, org.springframework.data.domain.PageRequest.of(page, size, Sort.by(dir, sort))));
    }

    @PostMapping
    public ResponseEntity<InboundResponseDTO> create(@RequestBody InboundRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InboundResponseDTO> update(@PathVariable Long id, @RequestBody InboundRequestDTO dto) {
        return service.update(id, dto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
