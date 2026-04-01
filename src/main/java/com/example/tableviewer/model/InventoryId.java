package com.example.tableviewer.model;

import java.io.Serializable;
import java.util.Objects;

public class InventoryId implements Serializable {

    private String isbn;

    private Long locationId;

    public InventoryId() {}

    public InventoryId(String isbn, Long locationId) {
        this.isbn = isbn;
        this.locationId = locationId;
    }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryId that = (InventoryId) o;
        return Objects.equals(isbn, that.isbn) && Objects.equals(locationId, that.locationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, locationId);
    }
}
