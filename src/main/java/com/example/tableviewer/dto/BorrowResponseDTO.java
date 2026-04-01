package com.example.tableviewer.dto;

import com.example.tableviewer.model.Borrow;
import com.example.tableviewer.model.BorrowId;
import java.time.LocalDate;

public class BorrowResponseDTO {

    private String isbn;
    private Long locationId;
    private Long readerId;
    private Long userId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate renewDate;
    private String status;

    public static BorrowResponseDTO from(Borrow borrow) {
        if (borrow == null) return null;
        BorrowResponseDTO dto = new BorrowResponseDTO();
        dto.setIsbn(borrow.getId().getIsbn());
        dto.setLocationId(borrow.getId().getLocationId());
        dto.setReaderId(borrow.getId().getReaderId());
        dto.setUserId(borrow.getUserId());
        dto.setBorrowDate(borrow.getBorrowDate());
        dto.setDueDate(borrow.getDueDate());
        dto.setRenewDate(borrow.getRenewDate());
        dto.setStatus(borrow.getStatus());
        return dto;
    }

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
