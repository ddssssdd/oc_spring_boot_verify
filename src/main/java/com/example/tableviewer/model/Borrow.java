package com.example.tableviewer.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_borrow")
public class Borrow {

    @EmbeddedId
    private BorrowId id;

    private Long userId;

    @Column(name = "borrow_date")
    private LocalDate borrowDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "renew_date")
    private LocalDate renewDate;

    @Column(length = 20)
    private String status;

    public Borrow() {}

    public BorrowId getId() { return id; }
    public void setId(BorrowId id) { this.id = id; }

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
