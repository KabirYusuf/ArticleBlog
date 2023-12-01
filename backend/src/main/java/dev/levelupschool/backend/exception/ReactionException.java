package dev.levelupschool.backend.exception;

import dev.levelupschool.backend.util.UserArticleReactionId;

public class ReactionException extends RuntimeException{
    public ReactionException(String message) {
        super(message);
    }
}
