package com.example.tableviewer.service;

import com.example.tableviewer.dto.BookRequestDTO;
import com.example.tableviewer.dto.BookResponseDTO;
import com.example.tableviewer.mapper.BookMapper;
import com.example.tableviewer.model.Book;
import com.example.tableviewer.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setIsbn("978-7-111-12345-6");
        book.setName("Java编程思想");
        book.setAuthor("Bruce Eckel");
        book.setDescription("Java经典书籍");
        book.setPublishedDate(LocalDate.of(2007, 3, 1));
        book.setPrice(new BigDecimal("108.00"));
        book.setInventoryQty(50);
    }

    @Test
    void findAll_shouldReturnPageOfBooks() {
        List<Book> books = List.of(book);
        Page<Book> page = new PageImpl<>(books, PageRequest.of(0, 10), 1);
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<BookResponseDTO> result = bookService.findAll(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Java编程思想", result.getContent().get(0).getName());
        verify(bookRepository).findAll(any(Pageable.class));
    }

    @Test
    void findById_whenExists_shouldReturnBook() {
        when(bookRepository.findById("978-7-111-12345-6")).thenReturn(Optional.of(book));

        Optional<BookResponseDTO> result = bookService.findById("978-7-111-12345-6");

        assertTrue(result.isPresent());
        assertEquals("Java编程思想", result.get().getName());
        assertEquals("Bruce Eckel", result.get().getAuthor());
    }

    @Test
    void findById_whenNotExists_shouldReturnEmpty() {
        when(bookRepository.findById("not-exist")).thenReturn(Optional.empty());

        Optional<BookResponseDTO> result = bookService.findById("not-exist");

        assertTrue(result.isEmpty());
    }

    @Test
    void findByName_whenExists_shouldReturnBook() {
        when(bookRepository.findByName("Java编程思想")).thenReturn(Optional.of(book));

        Optional<BookResponseDTO> result = bookService.findByName("Java编程思想");

        assertTrue(result.isPresent());
        assertEquals("978-7-111-12345-6", result.get().getIsbn());
    }

    @Test
    void findByName_whenNotExists_shouldReturnEmpty() {
        when(bookRepository.findByName("不存在")).thenReturn(Optional.empty());

        Optional<BookResponseDTO> result = bookService.findByName("不存在");

        assertTrue(result.isEmpty());
    }

    @Test
    void create_shouldSaveAndReturnBook() {
        BookRequestDTO dto = new BookRequestDTO();
        dto.setIsbn("978-7-115-98765-4");
        dto.setName("Python Crash Course");
        dto.setAuthor("Eric Matthes");
        dto.setDescription("Python入门经典");
        dto.setPublishedDate(LocalDate.of(2019, 5, 15));
        dto.setPrice(new BigDecimal("89.00"));
        dto.setInventoryQty(30);

        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArgument(0));

        BookResponseDTO result = bookService.create(dto);

        assertNotNull(result);
        assertEquals("978-7-115-98765-4", result.getIsbn());
        assertEquals("Python Crash Course", result.getName());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void update_whenExists_shouldUpdateAndReturn() {
        BookRequestDTO dto = new BookRequestDTO();
        dto.setName("Java编程思想（第4版）");
        dto.setAuthor("Bruce Eckel");
        dto.setDescription("更新版");
        dto.setPublishedDate(LocalDate.of(2007, 3, 1));
        dto.setPrice(new BigDecimal("128.00"));
        dto.setInventoryQty(30);

        when(bookRepository.findById("978-7-111-12345-6")).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArgument(0));

        Optional<BookResponseDTO> result = bookService.update("978-7-111-12345-6", dto);

        assertTrue(result.isPresent());
        assertEquals("Java编程思想（第4版）", result.get().getName());
        assertEquals(new BigDecimal("128.00"), result.get().getPrice());
    }

    @Test
    void update_whenNotExists_shouldReturnEmpty() {
        BookRequestDTO dto = new BookRequestDTO();
        dto.setName("不存在");

        when(bookRepository.findById("not-exist")).thenReturn(Optional.empty());

        Optional<BookResponseDTO> result = bookService.update("not-exist", dto);

        assertTrue(result.isEmpty());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void delete_whenExists_shouldReturnTrue() {
        when(bookRepository.existsById("978-7-111-12345-6")).thenReturn(true);

        boolean result = bookService.delete("978-7-111-12345-6");

        assertTrue(result);
        verify(bookRepository).deleteById("978-7-111-12345-6");
    }

    @Test
    void delete_whenNotExists_shouldReturnFalse() {
        when(bookRepository.existsById("not-exist")).thenReturn(false);

        boolean result = bookService.delete("not-exist");

        assertFalse(result);
        verify(bookRepository, never()).deleteById(any());
    }
}
