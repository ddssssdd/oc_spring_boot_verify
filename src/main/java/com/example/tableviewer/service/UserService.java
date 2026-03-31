package com.example.tableviewer.service;

import com.example.tableviewer.dto.UserRequest;
import com.example.tableviewer.dto.UserResponse;
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
    public Page<UserResponse> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserResponse::from);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponse> findById(Long id) {
        return userRepository.findById(id).map(UserResponse::from);
    }

    public UserResponse create(UserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAddress(request.getAddress());
        user.setPassword(request.getPassword());
        user.setBirthday(request.getBirthday());
        return UserResponse.from(userRepository.save(user));
    }

    public Optional<UserResponse> update(Long id, UserRequest request) {
        return userRepository.findById(id).map(user -> {
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setAddress(request.getAddress());
            user.setPassword(request.getPassword());
            user.setBirthday(request.getBirthday());
            return UserResponse.from(userRepository.save(user));
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
