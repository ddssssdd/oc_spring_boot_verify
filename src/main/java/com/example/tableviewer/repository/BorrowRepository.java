package com.example.tableviewer.repository;

import com.example.tableviewer.model.Borrow;
import com.example.tableviewer.model.BorrowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, BorrowId> {

    List<Borrow> findByIsbn(String isbn);
}
