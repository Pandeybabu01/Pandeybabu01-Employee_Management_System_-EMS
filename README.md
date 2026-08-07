# Employee Management System

A CRUD web application for managing employee records, built with **Spring MVC**, **Hibernate (Spring Data JPA)**, **MySQL**, and **Thymeleaf**, with **role-based access control** enforced via **Spring Security**.

## Features

- Add, view, update, and delete employee records
- Search employees by name, email, department, or designation
- Role-based access control with three roles:
  | Action | Admin | Manager | Employee |
  |---|---|---|---|
  | View employees | ✅ | ✅ | ✅ |
  | Add / update employees | ✅ | ✅ | ❌ |
  | Delete employees | ✅ | ❌ | ❌ |
  | Manage user accounts | ✅ | ❌ | ❌ |
- Form validation (required fields, valid email, positive salary, etc.)
- Password hashing with BCrypt
- Seed data + demo accounts created automatically on first run

## Tech Stack

- Java 17
- Spring Boot 3.2.5 (Spring MVC, Spring Security, Spring Data JPA)
- Hibernate (as the JPA provider)
- MySQL 8
- Thymeleaf (server-rendered views)
- Maven

## Project Structure

```
src/main/java/com/example/ems/
  ├── EmsApplication.java          # entry point
  ├── config/
  │   ├── SecurityConfig.java      # role-based URL authorization, login/logout
  │   └── DataInitializer.java     # seeds admin/manager/employee accounts + sample data
  ├── controller/
  │   ├── HomeController.java      # login page, dashboard
  │   ├── EmployeeController.java  # employee CRUD endpoints
  │   └── UserAdminController.java # user account management (admin only)
  ├── model/
  │   ├── Employee.java
  │   ├── AppUser.java
  │   └── Role.java
  ├── repository/
  │   ├── EmployeeRepository.java
  │   └── AppUserRepository.java
  ├── security/
  │   └── CustomUserDetailsService.java
  └── service/
      ├── EmployeeService.java
      └── EmployeeServiceImpl.java

src/main/resources/
  ├── application.properties       # DB connection + JPA config
  ├── db/schema.sql                # optional manual schema reference
  ├── static/css/style.css
  └── templates/                   # Thymeleaf views
```

## Setup & Run

### 1. Prerequisites
- JDK 17+
- Maven 3.8+
- MySQL 8 running locally (or update the connection URL for a remote instance)

### 2. Create the database
Hibernate will auto-create the tables, but you need the database itself to exist first (or leave `createDatabaseIfNotExist=true` in the URL, which is already set):

```sql
CREATE DATABASE ems_db;
```

### 3. Configure the connection
Edit `src/main/resources/application.properties` if your MySQL credentials differ from the defaults:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ems_db?useSSL=false&serverTimezone=UTC&createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root
```

### 4. Build and run

```bash
mvn clean install
mvn spring-boot:run
```

The app starts on **http://localhost:8080**.

### 5. Log in

On first startup, three demo accounts are seeded automatically:

| Username | Password     | Role     |
|----------|--------------|----------|


**Change or remove these credentials before deploying anywhere real** — they exist purely to make the app usable out of the box. New accounts can also be created from **Manage Users** (admin only) once logged in.

## Notes on the security model

- Authorization is enforced in two places for defense in depth: URL-level rules in `SecurityConfig` and method-level `@PreAuthorize` checks on controller methods.
- Passwords are stored as BCrypt hashes, never in plain text.
- `spring.jpa.hibernate.ddl-auto=update` is convenient for development. For production, switch to `validate` and manage schema changes with a migration tool such as Flyway or Liquibase.
