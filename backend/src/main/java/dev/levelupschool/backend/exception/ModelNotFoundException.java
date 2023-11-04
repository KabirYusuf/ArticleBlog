package dev.levelupschool.backend.exception;

public class ModelNotFoundException extends RuntimeException {
    public ModelNotFoundException(Long id) {
        super("Cannot find model with id: " + id);
    }
}
