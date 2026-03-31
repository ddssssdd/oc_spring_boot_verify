package com.example.tableviewer.service;

import com.example.tableviewer.dto.UserRequest;
import com.example.tableviewer.dto.UserResponse;
import com.example.tableviewer.model.User;
import com.example.tableviewer.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("张三");
        user.setEmail("zhangsan@example.com");
        user.setAddress("北京");
        user.setPassword("pass123");
        user.setBirthday(LocalDate.of(1990, 5, 15));
        user.setJoinDate(LocalDateTime.of(2026, 3, 31, 10, 0, 0));
    }

    @Test
    void findAll_shouldReturnPageOfUsers() {
        List<User> users = List.of(user);
        Page<User> page = new PageImpl<>(users, PageRequest.of(0, 10), 1);
        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<UserResponse> result = userService.findAll(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1L, result.getTotalElements());
        assertEquals("张三", result.getContent().get(0).getName());
        verify(userRepository).findAll(any(Pageable.class));
    }

    @Test
    void findById_whenExists_shouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserResponse> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("张三", result.get().getName());
        assertEquals("zhangsan@example.com", result.get().getEmail());
    }

    @Test
    void findById_whenNotExists_shouldReturnEmpty() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<UserResponse> result = userService.findById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void create_shouldSaveAndReturnUser() {
        UserRequest request = new UserRequest();
        request.setName("李四");
        request.setEmail("lisi@example.com");
        request.setAddress("上海");
        request.setPassword("pass456");
        request.setBirthday(LocalDate.of(1985, 8, 22));

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(2L);
            return u;
        });

        UserResponse result = userService.create(request);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("李四", result.getName());
        assertEquals("lisi@example.com", result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void update_whenExists_shouldUpdateAndReturn() {
        UserRequest request = new UserRequest();
        request.setName("张三改名");
        request.setEmail("new@example.com");
        request.setAddress("新地址");
        request.setPassword("newpass");
        request.setBirthday(LocalDate.of(1991, 1, 1));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        Optional<UserResponse> result = userService.update(1L, request);

        assertTrue(result.isPresent());
        assertEquals("张三改名", result.get().getName());
        assertEquals("new@example.com", result.get().getEmail());
    }

    @Test
    void update_whenNotExists_shouldReturnEmpty() {
        UserRequest request = new UserRequest();
        request.setName("测试");

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<UserResponse> result = userService.update(99L, request);

        assertTrue(result.isEmpty());
        verify(userRepository, never()).save(any());
    }

    @Test
    void delete_whenExists_shouldReturnTrue() {
        when(userRepository.existsById(1L)).thenReturn(true);

        boolean result = userService.delete(1L);

        assertTrue(result);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void delete_whenNotExists_shouldReturnFalse() {
        when(userRepository.existsById(99L)).thenReturn(false);

        boolean result = userService.delete(99L);

        assertFalse(result);
        verify(userRepository, never()).deleteById(any());
    }
}
