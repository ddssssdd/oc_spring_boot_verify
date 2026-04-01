package com.example.tableviewer.mapper;

import com.example.tableviewer.dto.InventoryRequestDTO;
import com.example.tableviewer.dto.InventoryResponseDTO;
import com.example.tableviewer.model.Inventory;
import com.example.tableviewer.model.InventoryId;

public class InventoryMapper {

    private InventoryMapper() {}

    public static InventoryResponseDTO toDTO(Inventory inventory) {
        if (inventory == null) return null;
        InventoryResponseDTO dto = new InventoryResponseDTO();
        dto.setIsbn(inventory.getId().getIsbn());
        dto.setLocationId(inventory.getId().getLocationId());
        dto.setQty(inventory.getQty());
        dto.setUpdateDate(inventory.getUpdateDate());
        return dto;
    }

    public static Inventory toModel(InventoryRequestDTO dto) {
        if (dto == null) return null;
        Inventory inventory = new Inventory();
        inventory.setId(new InventoryId(dto.getIsbn(), dto.getLocationId()));
        inventory.setQty(dto.getQty());
        inventory.setUpdateDate(dto.getUpdateDate());
        return inventory;
    }
}
