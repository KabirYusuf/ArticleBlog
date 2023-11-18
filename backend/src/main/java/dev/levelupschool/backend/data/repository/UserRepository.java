package dev.levelupschool.backend.data.repository;

import dev.levelupschool.backend.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
