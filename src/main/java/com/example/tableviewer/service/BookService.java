package com.example.tableviewer.service;

import com.example.tableviewer.dto.BookRequestDTO;
import com.example.tableviewer.dto.BookResponseDTO;
import com.example.tableviewer.mapper.BookMapper;
import com.example.tableviewer.model.Book;
import com.example.tableviewer.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public Page<BookResponseDTO> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).map(BookMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<BookResponseDTO> findById(String isbn) {
        return bookRepository.findById(isbn).map(BookMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<BookResponseDTO> findByName(String name) {
        return bookRepository.findByName(name).map(BookMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<BookResponseDTO> search(String isbn, String name, String description, Pageable pageable) {
        return bookRepository.searchBooks(isbn, name, description, pageable).map(BookMapper::toDTO);
    }

    public BookResponseDTO create(BookRequestDTO dto) {
        Book book = BookMapper.toModel(dto);
        return BookMapper.toDTO(bookRepository.save(book));
    }

    public Optional<BookResponseDTO> update(String isbn, BookRequestDTO dto) {
        return bookRepository.findById(isbn).map(book -> {
            book.setName(dto.getName());
            book.setAuthor(dto.getAuthor());
            book.setDescription(dto.getDescription());
            book.setPublishedDate(dto.getPublishedDate());
            book.setPrice(dto.getPrice());
            book.setInventoryQty(dto.getInventoryQty());
            return BookMapper.toDTO(bookRepository.save(book));
        });
    }

    public boolean delete(String isbn) {
        if (bookRepository.existsById(isbn)) {
            bookRepository.deleteById(isbn);
            return true;
        }
        return false;
    }
}
