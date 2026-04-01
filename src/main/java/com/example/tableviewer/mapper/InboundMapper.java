package com.example.tableviewer.mapper;

import com.example.tableviewer.dto.InboundRequestDTO;
import com.example.tableviewer.dto.InboundResponseDTO;
import com.example.tableviewer.model.Inbound;

public class InboundMapper {

    private InboundMapper() {}

    public static InboundResponseDTO toDTO(Inbound inbound) {
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

    public static Inbound toModel(InboundRequestDTO dto) {
        if (dto == null) return null;
        Inbound inbound = new Inbound();
        inbound.setId(dto.getId());
        inbound.setIsbn(dto.getIsbn());
        inbound.setUserId(dto.getUserId());
        inbound.setLocationId(dto.getLocationId());
        inbound.setQty(dto.getQty());
        inbound.setReceivedDate(dto.getReceivedDate());
        inbound.setPutawayDate(dto.getPutawayDate());
        return inbound;
    }
}
