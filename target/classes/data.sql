-- t_demo table creation
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

-- initial data
INSERT INTO t_demo (name, email, address, password, birthday) VALUES
('张三', 'zhangsan@example.com', '北京市朝阳区建国路88号', 'pass123', '1990-05-15'),
('李四', 'lisi@example.com', '上海市浦东新区世纪大道1000号', 'pass456', '1985-08-22'),
('王五', 'wangwu@example.com', '广州市天河区珠江新城花城大道', 'pass789', '1992-03-08'),
('赵六', 'zhaoliu@example.com', '深圳市南山区科技园南区', 'passabc', '1988-11-30'),
('孙七', 'sunqi@example.com', '杭州市西湖区文二路391号', 'passdef', '1995-07-18');
