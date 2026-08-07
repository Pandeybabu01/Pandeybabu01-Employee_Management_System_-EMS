-- Optional reference script.
-- The application auto-creates/updates these tables via Hibernate (spring.jpa.hibernate.ddl-auto=update),
-- so running this manually is NOT required. It's provided for reference / manual setup if preferred.

CREATE DATABASE IF NOT EXISTS ems_db;
USE ems_db;

CREATE TABLE IF NOT EXISTS app_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    department VARCHAR(60) NOT NULL,
    designation VARCHAR(60) NOT NULL,
    salary DOUBLE NOT NULL,
    date_of_joining DATE NOT NULL
);
