package com.example.tableviewer.repository;

import com.example.tableviewer.model.Borrow;
import com.example.tableviewer.model.BorrowId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, BorrowId> {

    List<Borrow> findByIsbn(String isbn);

    @Query("SELECT b FROM Borrow b WHERE " +
           "(:isbn IS NULL OR :isbn = '' OR b.id.isbn LIKE %:isbn%) AND " +
           "(:locationId IS NULL OR b.id.locationId = :locationId)")
    Page<Borrow> search(@Param("isbn") String isbn, @Param("locationId") Long locationId, Pageable pageable);
}
