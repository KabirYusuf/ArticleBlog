package dev.levelupschool.backend.service.implementation;

import dev.levelupschool.backend.data.dto.request.CreateAuthorRequest;
import dev.levelupschool.backend.data.dto.request.UpdateAuthorRequest;
import dev.levelupschool.backend.data.dto.response.CreateAuthorResponse;
import dev.levelupschool.backend.data.model.Author;
import dev.levelupschool.backend.data.repository.AuthorRepository;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.service.interfaces.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelUpAuthorService implements AuthorService {
    private final AuthorRepository authorRepository;
    public LevelUpAuthorService(AuthorRepository authorRepository){
        this.authorRepository = authorRepository;
    }

    @Override
    public CreateAuthorResponse createAuthor(CreateAuthorRequest createAuthorRequest) {
        Author newAuthor = new Author();
        newAuthor.setName(createAuthorRequest.getName());
        Author savedAuthor = authorRepository.save(newAuthor);
        CreateAuthorResponse createAuthorResponse = new CreateAuthorResponse();
        createAuthorResponse.setName(savedAuthor.getName());
        createAuthorResponse.setId(savedAuthor.getId());
        return createAuthorResponse;
    }

    @Override
    public Author findAuthorById(Long authorId) {
        return authorRepository
            .findById(authorId)
            .orElseThrow(()-> new ModelNotFoundException(authorId));
    }

    @Override
    public List<Author> findAllAuthors() {
        return authorRepository
            .findAll();
    }

    @Override
    public void deleteAuthor(Long authorId) {
        authorRepository
            .deleteById(authorId);
    }

    @Override
    public Author updateAuthor(UpdateAuthorRequest updateAuthorRequest, Long authorId) {
        Author foundAuthor = findAuthorById(authorId);
        foundAuthor.setName(updateAuthorRequest.getName());
        return authorRepository.save(foundAuthor);
    }
}
