package dev.levelupschool.backend.data.model;

import dev.levelupschool.backend.data.model.enums.ReactionType;
import dev.levelupschool.backend.util.UserArticleReactionId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_article_reactions")
@Setter
@Getter
public class UserArticleReaction {
    @EmbeddedId
    private UserArticleReactionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("articleId")
    private Article article;

    @Enumerated(EnumType.STRING)
    private ReactionType reaction;


}

