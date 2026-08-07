package com.example.ems.service;

import com.example.ems.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    List<Employee> findAll();

    List<Employee> search(String keyword);

    Optional<Employee> findById(Long id);

    Employee save(Employee employee);

    void deleteById(Long id);

    boolean emailExists(String email);
}
