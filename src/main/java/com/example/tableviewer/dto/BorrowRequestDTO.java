package com.example.tableviewer.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class BorrowRequestDTO {

    @NotBlank(message = "ISBN不能为空")
    private String isbn;

    @NotNull(message = "库位ID不能为空")
    private Long locationId;

    @NotNull(message = "读者ID不能为空")
    private Long readerId;

    private Long userId;

    private LocalDate borrowDate;

    private LocalDate dueDate;

    private LocalDate renewDate;

    @Size(max = 20, message = "状态长度不能超过20")
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
