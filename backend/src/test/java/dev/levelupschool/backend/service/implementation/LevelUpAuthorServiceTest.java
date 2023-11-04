package dev.levelupschool.backend.service.implementation;
import dev.levelupschool.backend.data.dto.request.CreateAuthorRequest;
import dev.levelupschool.backend.data.dto.request.UpdateAuthorRequest;
import dev.levelupschool.backend.data.model.Author;
import dev.levelupschool.backend.data.repository.AuthorRepository;
import dev.levelupschool.backend.service.interfaces.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LevelUpAuthorServiceTest {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private AuthorService authorService;
    private CreateAuthorRequest createAuthorRequest;
    @BeforeEach
    void setUp(){
        createAuthorRequest = new CreateAuthorRequest();
        createAuthorRequest.setName("Kabir Yusuf");
    }
    @AfterEach
    void deleteDatabaseData(){
        authorRepository.deleteAll();
    }

    @Test
    public void givenCreateAuthorRequest_whenICreateAuthor_theTableSizeOfAuthorIncreasesByOne(){
        int numberOfAuthorBeforeCreatingAnAuthor = authorService.findAllAuthors().size();
        assertEquals(0, numberOfAuthorBeforeCreatingAnAuthor);

        authorService.createAuthor(createAuthorRequest);

        int numberOfAuthorsAfterCreatingAnAuthor = authorService.findAllAuthors().size();
        assertEquals(1, numberOfAuthorsAfterCreatingAnAuthor);
    }
    @Test
    public void givenIHaveAuthors_whenIFindAnAuthorWithId_theAuthorWithThatIdIsReturned(){
        authorService.createAuthor(createAuthorRequest);
        Author foundAuthor = authorService.findAuthorById(1L);
        assertNotNull(foundAuthor);
    }
    @Test
    public void givenIHaveAuthors_whenIDeleteAnAuthor_theTableSizeOfAuthorDecreasesByOne(){
        authorService.createAuthor(createAuthorRequest);
        int numberOfAuthorsBeforeDeletingAnAuthor = authorService.findAllAuthors().size();
        assertEquals(1, numberOfAuthorsBeforeDeletingAnAuthor);

        authorService.deleteAuthor(1L);

        int numberOfAuthorsAfterDeletingAnAuthor = authorService.findAllAuthors().size();
        assertEquals(0, numberOfAuthorsAfterDeletingAnAuthor);
    }
    @Test
    public void givenIHaveAnAuthor_whenIUpdateTheAuthor_theUpdateAuthorIsReturned(){
        authorService.createAuthor(createAuthorRequest);
        String nameOfAuthorBeforeUpdatingAuthor = authorService.findAuthorById(1L).getName();
        assertEquals("Kabir Yusuf", nameOfAuthorBeforeUpdatingAuthor);

        UpdateAuthorRequest updateAuthorRequest = new UpdateAuthorRequest();
        updateAuthorRequest.setName("Luka Papez");

        Author updatedAuthor = authorService.updateAuthor(updateAuthorRequest, 1L);
        assertEquals("Luka Papez", updatedAuthor.getName());
    }

}
