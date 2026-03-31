package com.example.tableviewer.controller;

import com.example.tableviewer.dto.BookRequestDTO;
import com.example.tableviewer.dto.BookResponseDTO;
import com.example.tableviewer.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookRestController {

    private final BookService bookService;

    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    // GET /api/books?page=0&size=10&sort=isbn&direction=asc
    @GetMapping
    public ResponseEntity<Page<BookResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "isbn") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return ResponseEntity.ok(bookService.findAll(PageRequest.of(page, size, Sort.by(dir, sort))));
    }

    // GET /api/books/{isbn}
    @GetMapping("/{isbn}")
    public ResponseEntity<BookResponseDTO> findById(@PathVariable String isbn) {
        return bookService.findById(isbn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/books/search/by-name?name=xxx
    @GetMapping("/search/by-name")
    public ResponseEntity<BookResponseDTO> findByName(@RequestParam String name) {
        return bookService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/books
    @PostMapping
    public ResponseEntity<BookResponseDTO> create(@RequestBody BookRequestDTO dto) {
        BookResponseDTO created = bookService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/books/{isbn}
    @PutMapping("/{isbn}")
    public ResponseEntity<BookResponseDTO> update(@PathVariable String isbn, @RequestBody BookRequestDTO dto) {
        return bookService.update(isbn, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/books/{isbn}
    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> delete(@PathVariable String isbn) {
        if (bookService.delete(isbn)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
