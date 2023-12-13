package dev.levelupschool.backend.service.implementation;

import dev.levelupschool.backend.data.model.UserArticleReaction;
import dev.levelupschool.backend.data.repository.UserArticleReactionRepository;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.exception.ReactionException;
import dev.levelupschool.backend.service.interfaces.UserArticleReactionService;
import dev.levelupschool.backend.util.UserArticleReactionId;
import org.springframework.stereotype.Service;

@Service
public class LevelUpUserArticleReactionService implements UserArticleReactionService {
    private final UserArticleReactionRepository userArticleReactionRepository;
    public LevelUpUserArticleReactionService(UserArticleReactionRepository userArticleReactionRepository){
        this.userArticleReactionRepository = userArticleReactionRepository;
    }
    @Override
    public UserArticleReaction findUserArticleReactionById(UserArticleReactionId id) {
        return userArticleReactionRepository.findById(id).orElse(null);
    }

    @Override
    public UserArticleReaction save(UserArticleReaction userArticleReaction) {
        return userArticleReactionRepository.save(userArticleReaction);
    }

    @Override
    public void deleteUserArticleReaction(UserArticleReaction userArticleReaction) {
        userArticleReactionRepository.delete(userArticleReaction);
    }
}
