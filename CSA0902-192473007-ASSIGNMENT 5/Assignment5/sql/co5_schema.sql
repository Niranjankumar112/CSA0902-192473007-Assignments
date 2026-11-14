CREATE DATABASE IF NOT EXISTS co5_assignment;
USE co5_assignment;
CREATE TABLE IF NOT EXISTS bus_pass(
 pass_id VARCHAR(30) PRIMARY KEY,
 reg_no VARCHAR(30) NOT NULL,
 name VARCHAR(100) NOT NULL,
 route_no INT NOT NULL,
 amount DECIMAL(10,2) NOT NULL
);
CREATE TABLE IF NOT EXISTS users(
 id VARCHAR(50) PRIMARY KEY,
 pwd VARCHAR(255) NOT NULL
);
-- For local demonstration only; use a dummy password and never real credentials.
INSERT IGNORE INTO users(id,pwd) VALUES('demo','demo123');
