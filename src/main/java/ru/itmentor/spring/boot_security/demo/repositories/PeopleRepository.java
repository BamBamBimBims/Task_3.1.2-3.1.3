package ru.itmentor.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmentor.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    List<User> findAll();

    User save(User user);

    void deleteById(Long id);

    Optional<User> findById(Long id);

}
