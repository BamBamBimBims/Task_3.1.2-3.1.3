package ru.itmentor.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Optional;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {

    private final UserService userService;

    @Autowired
    public RestController(UserService userService) {
        this.userService = userService;
    }

    // ПОЛУЧЕНИЕ ВСЕХ ЮЗЕРОВ
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }


    // ДОБАВЛЕНИЕ НОВОГО ЮЗЕРА
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/users/save")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (!isValidUser(user)) {
            return ResponseEntity.badRequest().body(null);
        }
        User createdUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // УДАЛЕНИЕ ЮЗЕРА ПО ID
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/users/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ОБНАВЛЕНИЕ ЮЗЕРА ПО ID
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/users/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id,
                                           @RequestBody User user) {
        if (!isValidUser(user)) {
            return ResponseEntity.badRequest().body(null);
        }
        Optional<User> existingUserOptional = userService.getUserById(id);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setUserName(user.getUserName());
            existingUser.setLastName(user.getLastName());
            existingUser.setAge(user.getAge());
            existingUser.setPassword(user.getPassword());

            User updatedUser = userService.saveUser(existingUser);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ИНФО О ТЕКУЩЕМ ЮЗЕРЕ
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/user")
    public ResponseEntity<User> getUserInfo(Authentication authentication) {
        String userName = authentication.getName();
        Optional<User> user = userService.getUserByUsername(userName);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    private boolean isValidUser(User user) {
        if (user == null) {
            return false;
        } else {
            return true;
        }
    }
}
