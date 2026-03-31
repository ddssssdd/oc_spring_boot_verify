package com.example.tableviewer.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookQueryDTO {

    private String isbn;
    private String name;
    private String description;
    private int page = 0;
    private int size = 10;
    private String sort = "isbn";
    private String direction = "asc";

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public String getSort() { return sort; }
    public void setSort(String sort) { this.sort = sort; }

    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
}
