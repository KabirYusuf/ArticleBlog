package dev.levelupschool.backend.data.repository;

import dev.levelupschool.backend.data.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

}
