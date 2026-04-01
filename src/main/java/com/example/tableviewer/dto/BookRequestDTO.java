package com.example.tableviewer.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BookRequestDTO {

    @NotBlank(message = "ISBN不能为空")
    @Size(max = 20, message = "ISBN长度不能超过20")
    private String isbn;

    @NotBlank(message = "书名不能为空")
    @Size(max = 200, message = "书名长度不能超过200")
    private String name;

    @Size(max = 100, message = "作者长度不能超过100")
    private String author;

    @Size(max = 2000, message = "描述长度不能超过2000")
    private String description;

    private LocalDate publishedDate;

    @DecimalMin(value = "0.0", message = "价格不能为负数")
    private BigDecimal price;

    @Min(value = 0, message = "库存数量不能为负数")
    private Integer inventoryQty;

    @Size(max = 200, message = "出版社长度不能超过200")
    private String publisher;

    @Size(max = 500, message = "封面URL长度不能超过500")
    private String coverImageUrl;

    @Size(max = 2000, message = "推荐长度不能超过2000")
    private String comments;

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
