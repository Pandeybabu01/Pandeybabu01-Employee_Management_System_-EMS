package com.example.ems.model;

/**
 * Application roles.
 * ADMIN    - full CRUD access on employee records + user management
 * MANAGER  - can view, add and update employee records (no delete)
 * EMPLOYEE - read-only access to employee records
 */
public enum Role {
    ADMIN,
    MANAGER,
    EMPLOYEE
}
