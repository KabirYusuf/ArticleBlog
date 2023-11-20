package dev.levelupschool.backend.service.scheduler;

import dev.levelupschool.backend.service.interfaces.VerificationTokenService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public class VerificationTokenCleanUpService {
    private final VerificationTokenService verificationTokenService;

    public VerificationTokenCleanUpService(VerificationTokenService verificationTokenService) {
       this.verificationTokenService = verificationTokenService;
    }

    @Scheduled(fixedRate = 86400000)
    @Transactional
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        verificationTokenService.deleteTokenByExpiredAtBefore(now);
    }
}
