package com.example.tableviewer.repository;

import com.example.tableviewer.model.Inbound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InboundRepository extends JpaRepository<Inbound, Long> {

    List<Inbound> findByIsbn(String isbn);

    @Query("SELECT i FROM Inbound i WHERE " +
           "(:isbn IS NULL OR :isbn = '' OR i.isbn LIKE %:isbn%) AND " +
           "(:locationId IS NULL OR i.locationId = :locationId)")
    Page<Inbound> search(@Param("isbn") String isbn, @Param("locationId") Long locationId, Pageable pageable);
}
