package com.example.tableviewer.repository;

import com.example.tableviewer.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long> {

    List<Reader> findByNameContaining(String name);
}
