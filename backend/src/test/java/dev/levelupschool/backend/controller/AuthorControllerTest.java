package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.data.model.Author;
import dev.levelupschool.backend.data.repository.AuthorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthorControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private AuthorRepository authorRepository;
    @Test
    void contextLoads() {
    }

    @BeforeEach
    void setUp(){
        Author author = new Author();
        author.setName("kabir Yusuf");
        authorRepository.save(author);
    }
    @AfterEach
    void deleteDatabaseData(){
        authorRepository.deleteAll();
    }
    @Test
    public void givenAuthors_whenGetAuthors_thenReturnJsonArray() throws Exception {
        mvc.perform(
                get("/authors")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("kabir Yusuf")));
    }

    @Test
    public void givenAuthor_whenGetAuthorWithId_thenReturnJsonOfThatSpecificAuthor() throws Exception {
        mvc.perform(
                get("/authors/1")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("kabir Yusuf"));
    }

    @Test
    public void givenArticle_whenDeleteArticleWithId_then200IsReturnedAsStatusCode() throws Exception {
        mvc.perform(
                delete("/articles/1")
            )
            .andExpect(status().is2xxSuccessful());
    }

}
