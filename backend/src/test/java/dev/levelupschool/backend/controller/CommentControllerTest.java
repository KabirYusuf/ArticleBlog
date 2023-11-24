package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.data.model.Article;
import dev.levelupschool.backend.data.model.Author;
import dev.levelupschool.backend.data.model.Comment;
import dev.levelupschool.backend.data.repository.ArticleRepository;
import dev.levelupschool.backend.data.repository.AuthorRepository;
import dev.levelupschool.backend.data.repository.CommentRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CommentControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Test
    void contextLoads() {
    }
    @BeforeEach
    void setUp(){
        Author author = new Author();
        authorRepository.save(author);
        var article = new Article("test title 1", "test content 1", author);
        articleRepository.save(article);

        commentRepository.save(new Comment("test comment", article,author));
    }
    @AfterEach
    void deleteDatabaseData(){
        articleRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    public void givenComment_whenGetArticle_thenReturnCommentsArray() throws Exception {
        Author author = new Author();
        authorRepository.save(author);
        var article = articleRepository.save(new Article("test title", "test content 1", author));

        commentRepository.save(new Comment("test comment", article,author));

        mvc.perform(
                get("/articles/{id}", article.getId())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.comments", hasSize(1)));
    }
    @Test
    public void givenComment_whenGetCommentWithId_thenReturnJsonOfThatSpecificComment() throws Exception {
        mvc.perform(
                get("/comments/1")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").value("test comment"));
    }

    @Test
    public void givenComment_whenDeleteCommentWithId_then200IsReturnedAsStatusCode() throws Exception {
        mvc.perform(
                delete("/comments/1")
            )
            .andExpect(status().is2xxSuccessful());
    }

}
