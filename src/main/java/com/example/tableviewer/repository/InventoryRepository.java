package com.example.tableviewer.repository;

import com.example.tableviewer.model.Inventory;
import com.example.tableviewer.model.InventoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, InventoryId> {

    List<Inventory> findById_Isbn(String isbn);
}
