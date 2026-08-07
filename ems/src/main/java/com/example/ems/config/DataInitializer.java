package com.example.ems.config;

import com.example.ems.model.AppUser;
import com.example.ems.model.Employee;
import com.example.ems.model.Role;
import com.example.ems.repository.AppUserRepository;
import com.example.ems.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Seeds an initial ADMIN account and a couple of sample users/employees
 * so the app is usable immediately after first startup.
 *
 * Default admin login: admin / Admin@123  (change this after first login!)
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AppUserRepository appUserRepository,
                            EmployeeRepository employeeRepository,
                            PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedEmployees();
    }

    private void seedUsers() {
        if (!appUserRepository.existsByUsername("admin")) {
            appUserRepository.save(new AppUser("admin", passwordEncoder.encode("Admin@123"), Role.ADMIN));
        }
        if (!appUserRepository.existsByUsername("manager")) {
            appUserRepository.save(new AppUser("manager", passwordEncoder.encode("Manager@123"), Role.MANAGER));
        }
        if (!appUserRepository.existsByUsername("employee")) {
            appUserRepository.save(new AppUser("employee", passwordEncoder.encode("Employee@123"), Role.EMPLOYEE));
        }
    }

    private void seedEmployees() {
        if (employeeRepository.count() == 0) {
            employeeRepository.save(new Employee("Asha", "Verma", "asha.verma@example.com",
                    "+91 98765 43210", "Engineering", "Senior Software Engineer", 145000.0, LocalDate.of(2021, 3, 15)));
            employeeRepository.save(new Employee("Rohan", "Mehta", "rohan.mehta@example.com",
                    "+91 91234 56780", "Sales", "Regional Sales Manager", 110000.0, LocalDate.of(2019, 7, 1)));
            employeeRepository.save(new Employee("Priya", "Nair", "priya.nair@example.com",
                    "+91 99887 76655", "Human Resources", "HR Business Partner", 95000.0, LocalDate.of(2022, 1, 10)));
            employeeRepository.save(new Employee("Karan", "Singh", "karan.singh@example.com",
                    "+91 90000 11122", "Finance", "Financial Analyst", 88000.0, LocalDate.of(2023, 6, 5)));
        }
    }
}
