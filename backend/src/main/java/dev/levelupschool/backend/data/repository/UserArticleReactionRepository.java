package dev.levelupschool.backend.data.repository;

import dev.levelupschool.backend.data.model.UserArticleReaction;
import dev.levelupschool.backend.util.UserArticleReactionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserArticleReactionRepository extends JpaRepository<UserArticleReaction, UserArticleReactionId> {
}
