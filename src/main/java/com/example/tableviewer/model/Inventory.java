package com.example.tableviewer.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_inventory")
public class Inventory {

    @EmbeddedId
    private InventoryId id;

    @Column(nullable = false)
    private Integer qty;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    public Inventory() {}

    public InventoryId getId() { return id; }
    public void setId(InventoryId id) { this.id = id; }

    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }

    public LocalDateTime getUpdateDate() { return updateDate; }
    public void setUpdateDate(LocalDateTime updateDate) { this.updateDate = updateDate; }
}
