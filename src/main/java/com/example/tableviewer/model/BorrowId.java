package com.example.tableviewer.model;

import java.io.Serializable;
import java.util.Objects;

public class BorrowId implements Serializable {

    private String isbn;
    private Long locationId;
    private Long readerId;

    public BorrowId() {}

    public BorrowId(String isbn, Long locationId, Long readerId) {
        this.isbn = isbn;
        this.locationId = locationId;
        this.readerId = readerId;
    }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }

    public Long getReaderId() { return readerId; }
    public void setReaderId(Long readerId) { this.readerId = readerId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorrowId that = (BorrowId) o;
        return Objects.equals(isbn, that.isbn) && Objects.equals(locationId, that.locationId) && Objects.equals(readerId, that.readerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, locationId, readerId);
    }
}
