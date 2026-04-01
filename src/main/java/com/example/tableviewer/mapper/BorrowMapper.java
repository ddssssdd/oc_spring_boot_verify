package com.example.tableviewer.mapper;

import com.example.tableviewer.dto.BorrowRequestDTO;
import com.example.tableviewer.dto.BorrowResponseDTO;
import com.example.tableviewer.model.Borrow;
import com.example.tableviewer.model.BorrowId;

public class BorrowMapper {

    private BorrowMapper() {}

    public static BorrowResponseDTO toDTO(Borrow borrow) {
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

    public static Borrow toModel(BorrowRequestDTO dto) {
        if (dto == null) return null;
        Borrow borrow = new Borrow();
        borrow.setId(new BorrowId(dto.getIsbn(), dto.getLocationId(), dto.getReaderId()));
        borrow.setUserId(dto.getUserId());
        borrow.setBorrowDate(dto.getBorrowDate());
        borrow.setDueDate(dto.getDueDate());
        borrow.setRenewDate(dto.getRenewDate());
        borrow.setStatus(dto.getStatus());
        return borrow;
    }
}
