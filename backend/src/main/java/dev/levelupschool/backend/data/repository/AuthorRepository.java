package dev.levelupschool.backend.data.repository;

import dev.levelupschool.backend.data.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
