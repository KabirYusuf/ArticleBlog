package dev.levelupschool.backend.service.interfaces;

import dev.levelupschool.backend.data.model.VerificationToken;

import java.time.LocalDateTime;

public interface VerificationTokenService {
    VerificationToken createUniqueVerificationToken(Long userId);
    VerificationToken findByToken(String token);

    void deleteToken(Long id);
    void deleteTokenByExpiredAtBefore(LocalDateTime now);
}
