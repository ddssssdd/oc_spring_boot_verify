package com.example.tableviewer.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BorrowRequestDTO {

    private String isbn;
    private Long locationId;
    private Long readerId;
    private Long userId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate renewDate;
    private String status;

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }

    public Long getReaderId() { return readerId; }
    public void setReaderId(Long readerId) { this.readerId = readerId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getRenewDate() { return renewDate; }
    public void setRenewDate(LocalDate renewDate) { this.renewDate = renewDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
