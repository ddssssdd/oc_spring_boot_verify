package com.example.tableviewer.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "t_book")
public class Book {

    @Id
    @Column(name = "isbn", length = 20)
    private String isbn;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 100)
    private String author;

    @Column(length = 1000)
    private String description;

    @Column(name = "published_date")
    private LocalDate publishedDate;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "inventory_qty")
    private Integer inventoryQty;

    public Book() {}

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
}
