package com.example.tableviewer.mapper;

import com.example.tableviewer.dto.BookRequestDTO;
import com.example.tableviewer.dto.BookResponseDTO;
import com.example.tableviewer.model.Book;

public class BookMapper {

    private BookMapper() {}

    public static BookResponseDTO toDTO(Book book) {
        if (book == null) return null;
        BookResponseDTO dto = new BookResponseDTO();
        dto.setIsbn(book.getIsbn());
        dto.setName(book.getName());
        dto.setAuthor(book.getAuthor());
        dto.setDescription(book.getDescription());
        dto.setPublishedDate(book.getPublishedDate());
        dto.setPrice(book.getPrice());
        dto.setInventoryQty(book.getInventoryQty());
        return dto;
    }

    public static Book toModel(BookRequestDTO dto) {
        if (dto == null) return null;
        Book book = new Book();
        book.setIsbn(dto.getIsbn());
        book.setName(dto.getName());
        book.setAuthor(dto.getAuthor());
        book.setDescription(dto.getDescription());
        book.setPublishedDate(dto.getPublishedDate());
        book.setPrice(dto.getPrice());
        book.setInventoryQty(dto.getInventoryQty());
        return book;
    }
}
