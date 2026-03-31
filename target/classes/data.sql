-- =============================================
-- t_demo table
-- =============================================
DROP TABLE IF EXISTS t_demo;

CREATE TABLE t_demo (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(200),
    address VARCHAR(500),
    password VARCHAR(200),
    birthday DATE,
    joinDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO t_demo (name, email, address, password, birthday) VALUES
('张三', 'zhangsan@example.com', '北京市朝阳区建国路88号', 'pass123', '1990-05-15'),
('李四', 'lisi@example.com', '上海市浦东新区世纪大道1000号', 'pass456', '1985-08-22'),
('王五', 'wangwu@example.com', '广州市天河区珠江新城花城大道', 'pass789', '1992-03-08'),
('赵六', 'zhaoliu@example.com', '深圳市南山区科技园南区', 'passabc', '1988-11-30'),
('孙七', 'sunqi@example.com', '杭州市西湖区文二路391号', 'passdef', '1995-07-18');

-- =============================================
-- t_book table
-- =============================================
DROP TABLE IF EXISTS t_book;

CREATE TABLE t_book (
    isbn VARCHAR(20) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    author VARCHAR(100),
    description VARCHAR(1000),
    published_date DATE,
    price DECIMAL(10, 2),
    inventory_qty INTEGER
);

INSERT INTO t_book (isbn, name, author, description, published_date, price, inventory_qty) VALUES
('978-7-111-12345-6', 'Java编程思想', 'Bruce Eckel', 'Java经典书籍', '2007-03-01', 108.00, 50),
('978-7-115-98765-4', 'Python Crash Course', 'Eric Matthes', 'Python入门经典', '2019-05-15', 89.00, 30),
('978-5-13-456789-0', '算法导论', 'Thomas H. Cormen', '算法领域经典教材', '2012-07-26', 128.50, 20);
