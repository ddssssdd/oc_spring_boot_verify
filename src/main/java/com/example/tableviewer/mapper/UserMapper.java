package com.example.tableviewer.mapper;

import com.example.tableviewer.dto.UserRequestDTO;
import com.example.tableviewer.dto.UserResponseDTO;
import com.example.tableviewer.model.User;

public class UserMapper {

    private UserMapper() {}

    public static UserResponseDTO toDTO(User user) {
        if (user == null) return null;
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAddress(user.getAddress());
        dto.setBirthday(user.getBirthday());
        dto.setJoinDate(user.getJoinDate());
        return dto;
    }

    public static User toModel(UserRequestDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAddress(dto.getAddress());
        user.setPassword(dto.getPassword());
        user.setBirthday(dto.getBirthday());
        return user;
    }
}
