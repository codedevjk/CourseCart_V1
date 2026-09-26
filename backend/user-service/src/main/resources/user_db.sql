-- =========================================================================
-- USER SERVICE DATABASE
-- =========================================================================
CREATE DATABASE IF NOT EXISTS coursecart_user_db;
USE coursecart_user_db;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    UNIQUE KEY uk_users_username (username)
) ENGINE=InnoDB;

-- USER SERVICE SEED DATA
USE coursecart_user_db;

INSERT INTO users (id, name, username, password, role) VALUES 
(1001, 'Admin Cart', 'admin', 'password', 'ADMIN'),
(1002, 'Rahul Sharma', 'rahul_s', 'password', 'USER'),
(1003, 'Priya Patel', 'priya_p', 'password', 'USER'),
(1004, 'Amit Singh', 'amit_s', 'password', 'USER'),
(1005, 'Pooja Verma', 'pooja_v', 'password', 'USER'),
(1006, 'Rohan Gupta', 'rohan_g', 'password', 'USER');

