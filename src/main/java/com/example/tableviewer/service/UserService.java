package com.example.tableviewer.service;

import com.example.tableviewer.dto.UserRequestDTO;
import com.example.tableviewer.dto.UserResponseDTO;
import com.example.tableviewer.mapper.UserMapper;
import com.example.tableviewer.model.User;
import com.example.tableviewer.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponseDTO> findById(Long id) {
        return userRepository.findById(id).map(UserMapper::toDTO);
    }

    public UserResponseDTO create(UserRequestDTO dto) {
        User user = UserMapper.toModel(dto);
        return UserMapper.toDTO(userRepository.save(user));
    }

    public Optional<UserResponseDTO> update(Long id, UserRequestDTO dto) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(dto.getName());
                    user.setEmail(dto.getEmail());
                    user.setAddress(dto.getAddress());
                    user.setPassword(dto.getPassword());
                    user.setBirthday(dto.getBirthday());
                    return UserMapper.toDTO(userRepository.save(user));
                });
    }

    public boolean delete(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
