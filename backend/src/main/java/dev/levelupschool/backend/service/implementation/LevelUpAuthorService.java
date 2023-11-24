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
        String firstName = covertNameFirstCharacterToUpperCase(createAuthorRequest.getFirstName());
        String lastName = covertNameFirstCharacterToUpperCase(createAuthorRequest.getLastName());
        newAuthor.setFirstName(firstName);
        newAuthor.setLastName(lastName);
        Author savedAuthor = authorRepository.save(newAuthor);
        CreateAuthorResponse createAuthorResponse = new CreateAuthorResponse();
        createAuthorResponse.setName(savedAuthor.getFirstName() + " " + savedAuthor.getLastName());
        createAuthorResponse.setId(savedAuthor.getId());
        return createAuthorResponse;
    }

    private String covertNameFirstCharacterToUpperCase(String name){
        if (name != null) return name.substring(0,1).toUpperCase() + name.substring(1);
        return null;
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
        String firstName = covertNameFirstCharacterToUpperCase(updateAuthorRequest.getFirstName());
        String lastName = covertNameFirstCharacterToUpperCase(updateAuthorRequest.getLastName());
        foundAuthor.setFirstName(firstName);
        foundAuthor.setLastName(lastName);
        return authorRepository.save(foundAuthor);
    }
}
