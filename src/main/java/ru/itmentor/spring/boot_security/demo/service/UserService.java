package ru.itmentor.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmentor.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final MyUserDetalisService myUserDetalisService;

    @Autowired
    public UserService(MyUserDetalisService myUserDetalisService) {
        this.myUserDetalisService = myUserDetalisService;
    }

    public List<User> getAllUsers() {
        return myUserDetalisService.getAllUsers();
    }

    public User saveUser(User user) {
        return myUserDetalisService.saveUser(user);
    }

    public void deleteUser(Long id) {
        myUserDetalisService.deleteUser(id);
    }

    public User updateUser(User user) {
        return myUserDetalisService.updateUser(user);
    }

    public Optional<User> getUserById(Long id) {
        return myUserDetalisService.getUserById(id);
    }

    public Optional<User> getUserByUsername(String userName) {
        return myUserDetalisService.getUserByUsername(userName);
    }
}
