package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.data.model.Article;
import dev.levelupschool.backend.data.model.Author;
import dev.levelupschool.backend.data.repository.ArticleRepository;
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
class ArticleControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private AuthorRepository authorRepository;
    @Test
    void contextLoads() {
    }
    @BeforeEach
    void setUp(){
        Author author = new Author();
        authorRepository.save(author);
        var article = new Article("test title 1", "test content 1", author);

        articleRepository.save(article);
    }
    @AfterEach
    void deleteDatabaseData(){
        articleRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    public void givenArticle_whenGetArticles_thenReturnJsonArray() throws Exception {
        mvc.perform(
                get("/articles")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].title", is("test title 1")))
            .andExpect(jsonPath("$[0].author.id", is(1)));
    }

    @Test
    public void givenArticle_whenGetArticlesWithId_thenReturnJsonOfThatSpecificArticle() throws Exception {
        mvc.perform(
            get("/articles/1")
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("test title 1"));
    }

    @Test
    public void givenArticle_whenDeleteArticleWithId_then200IsReturnedAsStatusCode() throws Exception {
        mvc.perform(
                delete("/articles/1")
            )
            .andExpect(status().is2xxSuccessful());
    }



}
