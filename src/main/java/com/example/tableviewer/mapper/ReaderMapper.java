package com.example.tableviewer.mapper;

import com.example.tableviewer.dto.ReaderRequestDTO;
import com.example.tableviewer.dto.ReaderResponseDTO;
import com.example.tableviewer.model.Reader;

public class ReaderMapper {

    private ReaderMapper() {}

    public static ReaderResponseDTO toDTO(Reader reader) {
        if (reader == null) return null;
        ReaderResponseDTO dto = new ReaderResponseDTO();
        dto.setId(reader.getId());
        dto.setName(reader.getName());
        dto.setGender(reader.getGender());
        dto.setBirthday(reader.getBirthday());
        dto.setRegisteredDate(reader.getRegisteredDate());
        dto.setActiveFlag(reader.getActiveFlag());
        dto.setStatus(reader.getStatus());
        return dto;
    }

    public static Reader toModel(ReaderRequestDTO dto) {
        if (dto == null) return null;
        Reader reader = new Reader();
        reader.setId(dto.getId());
        reader.setName(dto.getName());
        reader.setGender(dto.getGender());
        reader.setBirthday(dto.getBirthday());
        reader.setRegisteredDate(dto.getRegisteredDate());
        reader.setActiveFlag(dto.getActiveFlag());
        reader.setStatus(dto.getStatus());
        return reader;
    }
}
