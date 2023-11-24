package dev.levelupschool.backend.service.interfaces;

import dev.levelupschool.backend.data.dto.request.CreateAuthorRequest;
import dev.levelupschool.backend.data.dto.request.UpdateAuthorRequest;
import dev.levelupschool.backend.data.dto.response.CreateAuthorResponse;
import dev.levelupschool.backend.data.model.Author;

import java.util.List;

public interface AuthorService {
    CreateAuthorResponse createAuthor(CreateAuthorRequest createAuthorRequest);
    Author findAuthorById(Long authorId);
    List<Author> findAllAuthors();
    void deleteAuthor(Long authorId);
    Author updateAuthor(UpdateAuthorRequest updateAuthorRequest, Long authorId);
}
