package com.example.tableviewer.controller;

import com.example.tableviewer.dto.UserRequestDTO;
import com.example.tableviewer.dto.UserResponseDTO;
import com.example.tableviewer.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserRestController userRestController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userRestController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(1L);
        userResponseDTO.setName("张三");
        userResponseDTO.setEmail("zhangsan@example.com");
        userResponseDTO.setAddress("北京");
        userResponseDTO.setBirthday(LocalDate.of(1990, 5, 15));
        userResponseDTO.setJoinDate(LocalDateTime.of(2026, 3, 31, 10, 0, 0));
    }

    @Test
    void findAll_shouldReturnPagedUsers() throws Exception {
        Page<UserResponseDTO> page = new PageImpl<>(
                List.of(userResponseDTO),
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")),
                1
        );
        when(userService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/users")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id")
                        .param("direction", "asc"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.totalElements").value(1))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.content[0].name").value("张三"));

        verify(userService).findAll(any(Pageable.class));
    }

    @Test
    void findById_whenExists_shouldReturnUser() throws Exception {
        when(userService.findById(1L)).thenReturn(Optional.of(userResponseDTO));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/users/1"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.name").value("张三"));

        verify(userService).findById(1L);
    }

    @Test
    void findById_whenNotExists_shouldReturn404() throws Exception {
        when(userService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/users/99"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isNotFound());

        verify(userService).findById(99L);
    }

    @Test
    void create_shouldReturn201() throws Exception {
        UserRequestDTO request = new UserRequestDTO();
        request.setName("李四");
        request.setEmail("lisi@example.com");
        request.setAddress("上海");
        request.setPassword("pass456");
        request.setBirthday(LocalDate.of(1985, 8, 22));

        UserResponseDTO created = new UserResponseDTO();
        created.setId(2L);
        created.setName("李四");
        created.setEmail("lisi@example.com");
        created.setAddress("上海");
        created.setBirthday(LocalDate.of(1985, 8, 22));

        when(userService.create(any(UserRequestDTO.class))).thenReturn(created);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isCreated())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.id").value(2))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.name").value("李四"));

        verify(userService).create(any(UserRequestDTO.class));
    }

    @Test
    void update_whenExists_shouldReturn200() throws Exception {
        UserRequestDTO request = new UserRequestDTO();
        request.setName("张三改");
        request.setEmail("new@example.com");

        when(userService.update(eq(1L), any(UserRequestDTO.class)))
                .thenReturn(Optional.of(userResponseDTO));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());

        verify(userService).update(eq(1L), any(UserRequestDTO.class));
    }

    @Test
    void update_whenNotExists_shouldReturn404() throws Exception {
        UserRequestDTO request = new UserRequestDTO();
        request.setName("测试");

        when(userService.update(eq(99L), any(UserRequestDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/users/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void delete_whenExists_shouldReturn204() throws Exception {
        when(userService.delete(1L)).thenReturn(true);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/users/1"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isNoContent());

        verify(userService).delete(1L);
    }

    @Test
    void delete_whenNotExists_shouldReturn404() throws Exception {
        when(userService.delete(99L)).thenReturn(false);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/users/99"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isNotFound());
    }
}
