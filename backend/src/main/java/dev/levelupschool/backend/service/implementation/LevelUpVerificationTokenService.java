package dev.levelupschool.backend.service.implementation;

import dev.levelupschool.backend.data.model.VerificationToken;
import dev.levelupschool.backend.data.repository.VerificationTokenRepository;
import dev.levelupschool.backend.service.interfaces.VerificationTokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
public class LevelUpVerificationTokenService implements VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;
    public LevelUpVerificationTokenService(VerificationTokenRepository verificationTokenRepository){
        this.verificationTokenRepository = verificationTokenRepository;
    }


    @Override
    public VerificationToken createUniqueVerificationToken(Long userId) {
        Random random = new Random();
        String token;
        VerificationToken verificationToken;

        do {
            token = String.valueOf(1000 + random.nextInt(9000));
            verificationToken = findByToken(token);
        } while (verificationToken != null);

        VerificationToken newToken = new VerificationToken();
        newToken.setToken(token);
        newToken.setExpiredAt(LocalDateTime.now().plusMinutes(10));
        newToken.setUserId(userId);

        return verificationTokenRepository.save(newToken);
    }

    @Override
    public VerificationToken findByToken(String token) {
        return verificationTokenRepository.findVerificationTokenByToken(token)
            .orElse(null);
    }

    @Override
    public void deleteToken(Long id) {
        verificationTokenRepository
            .deleteById(id);
    }

    @Override
    public void deleteTokenByExpiredAtBefore(LocalDateTime now) {
        verificationTokenRepository
            .deleteByExpiredAtBefore(now);
    }
}
