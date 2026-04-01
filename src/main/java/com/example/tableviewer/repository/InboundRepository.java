package com.example.tableviewer.repository;

import com.example.tableviewer.model.Inbound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InboundRepository extends JpaRepository<Inbound, Long> {

    List<Inbound> findByIsbn(String isbn);
}
