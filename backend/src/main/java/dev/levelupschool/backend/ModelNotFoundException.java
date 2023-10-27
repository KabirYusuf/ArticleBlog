package dev.levelupschool.backend;

public class ModelNotFoundException extends RuntimeException {
    ModelNotFoundException(Long id) {
        super("Cannot find model with id: " + id);
    }
}
