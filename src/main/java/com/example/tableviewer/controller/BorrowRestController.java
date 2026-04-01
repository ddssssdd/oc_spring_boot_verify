package com.example.tableviewer.controller;

import com.example.tableviewer.dto.BorrowRequestDTO;
import com.example.tableviewer.dto.BorrowResponseDTO;
import com.example.tableviewer.service.BorrowService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrows")
@CrossOrigin(origins = "*")
public class BorrowRestController {

    private final BorrowService service;

    public BorrowRestController(BorrowService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<BorrowResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "isbn") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return ResponseEntity.ok(service.findAll(PageRequest.of(page, size, Sort.by(dir, sort))));
    }

    @GetMapping("/{isbn}/{locationId}/{readerId}")
    public ResponseEntity<BorrowResponseDTO> findById(
            @PathVariable String isbn,
            @PathVariable Long locationId,
            @PathVariable Long readerId) {
        return service.findById(isbn, locationId, readerId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<BorrowResponseDTO>> findByIsbn(@RequestParam String isbn) {
        return ResponseEntity.ok(service.findByIsbn(isbn));
    }

    // GET /api/borrows/search/page?isbn=&locationId=&page=0&size=10
    @GetMapping("/search/page")
    public ResponseEntity<Page<BorrowResponseDTO>> searchPage(
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
    public ResponseEntity<BorrowResponseDTO> create(@Valid @RequestBody BorrowRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{isbn}/{locationId}/{readerId}")
    public ResponseEntity<BorrowResponseDTO> update(
            @PathVariable String isbn,
            @PathVariable Long locationId,
            @PathVariable Long readerId,
            @Valid @RequestBody BorrowRequestDTO dto) {
        return service.update(isbn, locationId, readerId, dto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{isbn}/{locationId}/{readerId}")
    public ResponseEntity<Void> delete(
            @PathVariable String isbn,
            @PathVariable Long locationId,
            @PathVariable Long readerId) {
        if (service.delete(isbn, locationId, readerId)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
