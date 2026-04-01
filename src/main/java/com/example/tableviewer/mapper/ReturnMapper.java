package com.example.tableviewer.mapper;

import com.example.tableviewer.dto.ReturnRequestDTO;
import com.example.tableviewer.dto.ReturnResponseDTO;
import com.example.tableviewer.model.Return;

public class ReturnMapper {

    private ReturnMapper() {}

    public static ReturnResponseDTO toDTO(Return ret) {
        if (ret == null) return null;
        ReturnResponseDTO dto = new ReturnResponseDTO();
        dto.setId(ret.getId());
        dto.setIsbn(ret.getIsbn());
        dto.setReaderId(ret.getReaderId());
        dto.setReturnDate(ret.getReturnDate());
        dto.setLocationId(ret.getLocationId());
        dto.setUserId(ret.getUserId());
        return dto;
    }

    public static Return toModel(ReturnRequestDTO dto) {
        if (dto == null) return null;
        Return ret = new Return();
        ret.setId(dto.getId());
        ret.setIsbn(dto.getIsbn());
        ret.setReaderId(dto.getReaderId());
        ret.setReturnDate(dto.getReturnDate());
        ret.setLocationId(dto.getLocationId());
        ret.setUserId(dto.getUserId());
        return ret;
    }
}
