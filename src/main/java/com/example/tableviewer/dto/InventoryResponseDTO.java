package com.example.tableviewer.dto;

import com.example.tableviewer.model.Inventory;
import com.example.tableviewer.model.InventoryId;
import java.time.LocalDateTime;

public class InventoryResponseDTO {

    private String isbn;
    private Long locationId;
    private Integer qty;
    private LocalDateTime updateDate;

    public static InventoryResponseDTO from(Inventory inventory) {
        if (inventory == null) return null;
        InventoryResponseDTO dto = new InventoryResponseDTO();
        dto.setIsbn(inventory.getId().getIsbn());
        dto.setLocationId(inventory.getId().getLocationId());
        dto.setQty(inventory.getQty());
        dto.setUpdateDate(inventory.getUpdateDate());
        return dto;
    }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }

    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }

    public LocalDateTime getUpdateDate() { return updateDate; }
    public void setUpdateDate(LocalDateTime updateDate) { this.updateDate = updateDate; }
}
