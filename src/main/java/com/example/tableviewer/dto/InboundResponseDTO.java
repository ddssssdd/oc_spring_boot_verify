package com.example.tableviewer.dto;

import com.example.tableviewer.model.Inbound;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class InboundResponseDTO {

    private Long id;
    private String isbn;
    private Long userId;
    private Long locationId;
    private Integer qty;
    private LocalDate receivedDate;
    private LocalDateTime putawayDate;

    public static InboundResponseDTO from(Inbound inbound) {
        if (inbound == null) return null;
        InboundResponseDTO dto = new InboundResponseDTO();
        dto.setId(inbound.getId());
        dto.setIsbn(inbound.getIsbn());
        dto.setUserId(inbound.getUserId());
        dto.setLocationId(inbound.getLocationId());
        dto.setQty(inbound.getQty());
        dto.setReceivedDate(inbound.getReceivedDate());
        dto.setPutawayDate(inbound.getPutawayDate());
        return dto;
    }

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
