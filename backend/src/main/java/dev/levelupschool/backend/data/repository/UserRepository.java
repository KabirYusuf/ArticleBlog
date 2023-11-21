package dev.levelupschool.backend.data.repository;

import dev.levelupschool.backend.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUsersByUsernameIgnoreCase(String email);
    Optional<User> findByUsernameIgnoreCase(String email);
}
