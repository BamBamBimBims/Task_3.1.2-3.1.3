package ru.itmentor.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.repositories.PeopleRepository;
import ru.itmentor.spring.boot_security.demo.security.MyUserDetails;

import java.util.List;
import java.util.Optional;

@Service
public class MyUserDetalisService implements UserDetailsService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public MyUserDetalisService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = peopleRepository.findByUserName(username);

        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new MyUserDetails(user.get());
    }

    public List<User> getAllUsers() {
        return peopleRepository.findAll();
    }

    public User saveUser(User user) {
        return peopleRepository.save(user);
    }

    public void deleteUser(Long id) {
        peopleRepository.deleteById(id);
    }


    public User updateUser(User user) {
        return peopleRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return peopleRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String userName) {
        return peopleRepository.findByUserName(userName);
    }
}
