-- Database indexes for performance optimization

-- Book table indexes
CREATE INDEX IF NOT EXISTS idx_book_name ON t_book(name);
CREATE INDEX IF NOT EXISTS idx_book_author ON t_book(author);

-- Inbound table indexes
CREATE INDEX IF NOT EXISTS idx_inbound_isbn ON t_inbound(isbn);
CREATE INDEX IF NOT EXISTS idx_inbound_location_id ON t_inbound(location_id);

-- Borrow table indexes
CREATE INDEX IF NOT EXISTS idx_borrow_isbn ON t_borrow(isbn);
CREATE INDEX IF NOT EXISTS idx_borrow_reader_id ON t_borrow(reader_id);
CREATE INDEX IF NOT EXISTS idx_borrow_location_id ON t_borrow(location_id);
CREATE INDEX IF NOT EXISTS idx_borrow_status ON t_borrow(status);

-- Return table indexes
CREATE INDEX IF NOT EXISTS idx_return_isbn ON t_return(isbn);
CREATE INDEX IF NOT EXISTS idx_return_reader_id ON t_return(reader_id);

-- Inventory table indexes
CREATE INDEX IF NOT EXISTS idx_inventory_isbn ON t_inventory(isbn);

-- Reader table indexes
CREATE INDEX IF NOT EXISTS idx_reader_name ON t_reader(name);

-- User table indexes
CREATE INDEX IF NOT EXISTS idx_user_username ON t_user(username);
