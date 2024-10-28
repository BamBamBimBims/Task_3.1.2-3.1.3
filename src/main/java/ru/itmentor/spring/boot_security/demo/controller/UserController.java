package ru.itmentor.spring.boot_security.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.UserService;


import java.util.List;
import java.util.Optional;


@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/index")
    public String sayIndex() {
        return "index";
    }


    @PreAuthorize("hasRole('ADMIN')") // ТАБЛИЦА ЮЗЕРОВ
    @GetMapping("/admin/users")
    public String showAllUsers(Model model) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("allUsers", allUsers);
        return "users";
    }

    @PreAuthorize("hasRole('ADMIN')") // ДОБАВЛЯЕМ ЮЗЕРОВ
    @GetMapping("/admin/add")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        return "addUser";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/add")
    public String addUser(@ModelAttribute User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "addUser";
        }
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @PreAuthorize("hasRole('ADMIN')") // УДАЛЯЕМ ЮЗЕРОВ
    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    @PreAuthorize("hasRole('ADMIN')") // ОБНОВЛЯЕМ ЮЗЕРОВ
    @GetMapping("/admin/update/{id}")
    public String updateUser(@PathVariable("id") Long id, Model model) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "updateUser";
        } else {
            return "redirect:/admin/users";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/update/{id}")
    public String updateUser(@ModelAttribute User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "updateUser";
        }
        userService.updateUser(user);
        return "redirect:/admin/users";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // ИНФОРМАЦИЯ ДЛЯ ЮЗЕРА
    @GetMapping("/user")
    public String getUserInfo(Authentication authentication, Model model) {
        String userName = authentication.getName();
        Optional<User> user = userService.getUserByUsername(userName);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user";
        } else {
            return "index";
        }
    }

    @GetMapping("/logout") // ВЫХОД ИЗ СИСТЕМЫ
    public String logout() {
        return "redirect:/index";
    }
}