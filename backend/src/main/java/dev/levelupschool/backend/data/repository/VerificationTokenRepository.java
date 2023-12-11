package dev.levelupschool.backend.data.repository;

import dev.levelupschool.backend.data.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findVerificationTokenByToken(String token);
    @Transactional
    void deleteByExpiredAtBefore(LocalDateTime now);
}
