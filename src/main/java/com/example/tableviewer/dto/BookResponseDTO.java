package com.example.tableviewer.dto;

import com.example.tableviewer.model.Book;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BookResponseDTO {

    private String isbn;
    private String name;
    private String author;
    private String description;
    private LocalDate publishedDate;
    private BigDecimal price;
    private Integer inventoryQty;
    private String publisher;
    private String coverImageUrl;
    private String comments;

    public static BookResponseDTO from(Book book) {
        if (book == null) return null;
        BookResponseDTO dto = new BookResponseDTO();
        dto.setIsbn(book.getIsbn());
        dto.setName(book.getName());
        dto.setAuthor(book.getAuthor());
        dto.setDescription(book.getDescription());
        dto.setPublishedDate(book.getPublishedDate());
        dto.setPrice(book.getPrice());
        dto.setInventoryQty(book.getInventoryQty());
        dto.setPublisher(book.getPublisher());
        dto.setCoverImageUrl(book.getCoverImageUrl());
        dto.setComments(book.getComments());
        return dto;
    }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getPublishedDate() { return publishedDate; }
    public void setPublishedDate(LocalDate publishedDate) { this.publishedDate = publishedDate; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getInventoryQty() { return inventoryQty; }
    public void setInventoryQty(Integer inventoryQty) { this.inventoryQty = inventoryQty; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}
