package com.example.ems.controller;

import com.example.ems.model.AppUser;
import com.example.ems.model.Role;
import com.example.ems.repository.AppUserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Lets an ADMIN create login accounts (username/password/role) for
 * managers and employees. Restricted to ADMIN both at the URL level
 * (SecurityConfig) and the method level (@PreAuthorize) as defense in depth.
 */
@Controller
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserAdminController {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAdminController(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", appUserRepository.findAll());
        model.addAttribute("roles", Role.values());
        return "users/list";
    }

    @PostMapping("/save")
    public String save(@RequestParam String username,
                        @RequestParam String password,
                        @RequestParam Role role,
                        RedirectAttributes redirectAttributes) {

        if (appUserRepository.existsByUsername(username)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Username already exists.");
            return "redirect:/users";
        }

        AppUser user = new AppUser(username, passwordEncoder.encode(password), role);
        appUserRepository.save(user);
        redirectAttributes.addFlashAttribute("successMessage", "User '" + username + "' created.");
        return "redirect:/users";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        appUserRepository.findById(id).ifPresent(u -> appUserRepository.deleteById(id));
        redirectAttributes.addFlashAttribute("successMessage", "User deleted.");
        return "redirect:/users";
    }
}
