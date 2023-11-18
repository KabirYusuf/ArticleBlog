package dev.levelupschool.backend.service.implementation;

import dev.levelupschool.backend.data.model.Token;
import dev.levelupschool.backend.data.repository.TokenRepository;
import dev.levelupschool.backend.service.interfaces.TokenService;
import org.springframework.stereotype.Service;

@Service
public class LevelUpTokenService implements TokenService {
    private final TokenRepository tokenRepository;
    public LevelUpTokenService(TokenRepository tokenRepository){
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Token saveToken(Token token) {
        return tokenRepository.save(token);
    }

}
