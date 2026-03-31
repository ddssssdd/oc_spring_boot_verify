package com.example.tableviewer.controller;

import com.example.tableviewer.dto.BookRequestDTO;
import com.example.tableviewer.dto.BookResponseDTO;
import com.example.tableviewer.service.BookService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookRestControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookRestController bookRestController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private BookResponseDTO bookResponseDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookRestController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        bookResponseDTO = new BookResponseDTO();
        bookResponseDTO.setIsbn("978-7-111-12345-6");
        bookResponseDTO.setName("Java编程思想");
        bookResponseDTO.setAuthor("Bruce Eckel");
        bookResponseDTO.setDescription("Java经典书籍");
        bookResponseDTO.setPublishedDate(LocalDate.of(2007, 3, 1));
        bookResponseDTO.setPrice(new BigDecimal("108.00"));
        bookResponseDTO.setInventoryQty(50);
    }

    @Test
    void findAll_shouldReturnPagedBooks() throws Exception {
        Page<BookResponseDTO> page = new PageImpl<>(
                List.of(bookResponseDTO),
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "isbn")),
                1
        );
        when(bookService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/books")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "isbn")
                        .param("direction", "asc"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.totalElements").value(1))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.content[0].name").value("Java编程思想"));

        verify(bookService).findAll(any(Pageable.class));
    }

    @Test
    void findById_whenExists_shouldReturnBook() throws Exception {
        when(bookService.findById("978-7-111-12345-6")).thenReturn(Optional.of(bookResponseDTO));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/books/978-7-111-12345-6"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.isbn").value("978-7-111-12345-6"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.name").value("Java编程思想"));

        verify(bookService).findById("978-7-111-12345-6");
    }

    @Test
    void findById_whenNotExists_shouldReturn404() throws Exception {
        when(bookService.findById("not-exist")).thenReturn(Optional.empty());

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/books/not-exist"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isNotFound());

        verify(bookService).findById("not-exist");
    }

    @Test
    void findByName_whenExists_shouldReturnBook() throws Exception {
        when(bookService.findByName("Java编程思想")).thenReturn(Optional.of(bookResponseDTO));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/books/search/by-name")
                        .param("name", "Java编程思想"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.isbn").value("978-7-111-12345-6"));

        verify(bookService).findByName("Java编程思想");
    }

    @Test
    void findByName_whenNotExists_shouldReturn404() throws Exception {
        when(bookService.findByName("不存在")).thenReturn(Optional.empty());

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/books/search/by-name")
                        .param("name", "不存在"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void create_shouldReturn201() throws Exception {
        BookRequestDTO request = new BookRequestDTO();
        request.setIsbn("978-7-115-98765-4");
        request.setName("Python Crash Course");
        request.setAuthor("Eric Matthes");
        request.setDescription("Python入门经典");
        request.setPublishedDate(LocalDate.of(2019, 5, 15));
        request.setPrice(new BigDecimal("89.00"));
        request.setInventoryQty(30);

        BookResponseDTO created = new BookResponseDTO();
        created.setIsbn("978-7-115-98765-4");
        created.setName("Python Crash Course");
        created.setAuthor("Eric Matthes");

        when(bookService.create(any(BookRequestDTO.class))).thenReturn(created);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isCreated())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.isbn").value("978-7-115-98765-4"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.name").value("Python Crash Course"));

        verify(bookService).create(any(BookRequestDTO.class));
    }

    @Test
    void update_whenExists_shouldReturn200() throws Exception {
        BookRequestDTO request = new BookRequestDTO();
        request.setName("Java编程思想（第4版）");
        request.setAuthor("Bruce Eckel");
        request.setPrice(new BigDecimal("128.00"));

        when(bookService.update(eq("978-7-111-12345-6"), any(BookRequestDTO.class)))
                .thenReturn(Optional.of(bookResponseDTO));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/books/978-7-111-12345-6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());

        verify(bookService).update(eq("978-7-111-12345-6"), any(BookRequestDTO.class));
    }

    @Test
    void update_whenNotExists_shouldReturn404() throws Exception {
        BookRequestDTO request = new BookRequestDTO();
        request.setName("不存在");

        when(bookService.update(eq("not-exist"), any(BookRequestDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/books/not-exist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void delete_whenExists_shouldReturn204() throws Exception {
        when(bookService.delete("978-7-111-12345-6")).thenReturn(true);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/books/978-7-111-12345-6"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isNoContent());

        verify(bookService).delete("978-7-111-12345-6");
    }

    @Test
    void delete_whenNotExists_shouldReturn404() throws Exception {
        when(bookService.delete("not-exist")).thenReturn(false);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/books/not-exist"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void search_withNoParams_shouldReturnAllBooks() throws Exception {
        Page<BookResponseDTO> page = new PageImpl<>(
                List.of(bookResponseDTO),
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "isbn")),
                1
        );
        when(bookService.search(any(), any(), any(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/books/search")
                        .param("isbn", "")
                        .param("name", "")
                        .param("description", ""))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.totalElements").value(1));

        verify(bookService).search(any(), any(), any(), any(Pageable.class));
    }

    @Test
    void search_withNameParam_shouldReturnFilteredBooks() throws Exception {
        Page<BookResponseDTO> page = new PageImpl<>(
                List.of(bookResponseDTO),
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "isbn")),
                1
        );
        when(bookService.search(any(), eq("Java"), any(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/books/search")
                        .param("name", "Java"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath("$.content[0].name").value("Java编程思想"));

        verify(bookService).search(any(), eq("Java"), any(), any(Pageable.class));
    }
}
