package com.example.tableviewer.repository;

import com.example.tableviewer.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    Optional<Book> findByName(String name);

    @Query("SELECT b FROM Book b WHERE " +
           "(:isbn IS NULL OR :isbn = '' OR b.isbn LIKE %:isbn%) AND " +
           "(:name IS NULL OR :name = '' OR b.name LIKE %:name%) AND " +
           "(:description IS NULL OR :description = '' OR b.description LIKE %:description%)")
    Page<Book> searchBooks(
            @Param("isbn") String isbn,
            @Param("name") String name,
            @Param("description") String description,
            Pageable pageable
    );
}
