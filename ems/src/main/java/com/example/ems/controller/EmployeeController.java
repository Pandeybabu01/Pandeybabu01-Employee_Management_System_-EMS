package com.example.ems.controller;

import com.example.ems.model.Employee;
import com.example.ems.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Viewable by ADMIN, MANAGER, EMPLOYEE
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public String list(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("employees", employeeService.search(keyword));
        model.addAttribute("keyword", keyword);
        return "employees/list";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public String view(@PathVariable Long id, Model model) {
        Employee employee = employeeService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with id: " + id));
        model.addAttribute("employee", employee);
        return "employees/view";
    }

    // Add/edit restricted to ADMIN and MANAGER
    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String newForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("formTitle", "Add Employee");
        return "employees/form";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String editForm(@PathVariable Long id, Model model) {
        Employee employee = employeeService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with id: " + id));
        model.addAttribute("employee", employee);
        model.addAttribute("formTitle", "Edit Employee");
        return "employees/form";
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public String save(@Valid @ModelAttribute("employee") Employee employee,
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("formTitle", employee.getId() == null ? "Add Employee" : "Edit Employee");
            return "employees/form";
        }

        // Guard against duplicate email when creating a new record or changing the email
        boolean emailTaken = employeeService.emailExists(employee.getEmail());
        if (emailTaken) {
            Employee existing = employeeService.findAll().stream()
                    .filter(e -> e.getEmail().equalsIgnoreCase(employee.getEmail()))
                    .findFirst().orElse(null);
            boolean sameRecord = existing != null && existing.getId().equals(employee.getId());
            if (!sameRecord) {
                result.rejectValue("email", "duplicate", "An employee with this email already exists");
                model.addAttribute("formTitle", employee.getId() == null ? "Add Employee" : "Edit Employee");
                return "employees/form";
            }
        }

        employeeService.save(employee);
        redirectAttributes.addFlashAttribute("successMessage",
                "Employee " + employee.getFullName() + " saved successfully.");
        return "redirect:/employees";
    }

    // Deletion restricted to ADMIN only
    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        employeeService.findById(id).ifPresent(employee -> {
            employeeService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Employee " + employee.getFullName() + " deleted successfully.");
        });
        return "redirect:/employees";
    }
}
