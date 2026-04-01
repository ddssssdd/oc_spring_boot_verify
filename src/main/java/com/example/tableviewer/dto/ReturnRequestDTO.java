package com.example.tableviewer.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class ReturnRequestDTO {

    private Long id;

    @NotBlank(message = "ISBN不能为空")
    private String isbn;

    @NotNull(message = "读者ID不能为空")
    private Long readerId;

    private LocalDate returnDate;

    private Long locationId;

    private Long userId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Long getReaderId() { return readerId; }
    public void setReaderId(Long readerId) { this.readerId = readerId; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
