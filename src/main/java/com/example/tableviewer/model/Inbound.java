package com.example.tableviewer.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_inbound")
public class Inbound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String isbn;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "location_id")
    private Long locationId;

    @Column(nullable = false)
    private Integer qty;

    @Column(name = "received_date")
    private LocalDate receivedDate;

    @Column(name = "putaway_date")
    private LocalDateTime putawayDate;

    public Inbound() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }

    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }

    public LocalDate getReceivedDate() { return receivedDate; }
    public void setReceivedDate(LocalDate receivedDate) { this.receivedDate = receivedDate; }

    public LocalDateTime getPutawayDate() { return putawayDate; }
    public void setPutawayDate(LocalDateTime putawayDate) { this.putawayDate = putawayDate; }
}
