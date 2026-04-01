-- Borrow initial data
INSERT INTO t_borrow (isbn, location_id, reader_id, user_id, borrow_date, due_date, renew_date, status) VALUES ('978-7-111-54742-1', 1, 1, 1, '2024-01-15', '2024-02-15', NULL, 'BORROWED');
INSERT INTO t_borrow (isbn, location_id, reader_id, user_id, borrow_date, due_date, renew_date, status) VALUES ('978-7-111-54742-2', 1, 2, 1, '2024-01-20', '2024-02-20', NULL, 'BORROWED');
