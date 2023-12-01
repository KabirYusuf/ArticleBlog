package dev.levelupschool.backend.service.interfaces;

import dev.levelupschool.backend.data.model.UserArticleReaction;
import dev.levelupschool.backend.util.UserArticleReactionId;

public interface UserArticleReactionService {
    UserArticleReaction findUserArticleReactionById(UserArticleReactionId id);
    UserArticleReaction save(UserArticleReaction userArticleReaction);
    void deleteUserArticleReaction(UserArticleReaction userArticleReaction);
}
