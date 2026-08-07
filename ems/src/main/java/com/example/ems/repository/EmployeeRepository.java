package com.example.ems.repository;

import com.example.ems.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    @Query("select e from Employee e where " +
           "lower(e.firstName) like lower(concat('%', :keyword, '%')) or " +
           "lower(e.lastName) like lower(concat('%', :keyword, '%')) or " +
           "lower(e.email) like lower(concat('%', :keyword, '%')) or " +
           "lower(e.department) like lower(concat('%', :keyword, '%')) or " +
           "lower(e.designation) like lower(concat('%', :keyword, '%'))")
    List<Employee> search(@Param("keyword") String keyword);
}
