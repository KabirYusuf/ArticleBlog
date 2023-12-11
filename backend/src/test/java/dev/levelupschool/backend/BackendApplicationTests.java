package dev.levelupschool.backend;

import dev.levelupschool.backend.data.model.Article;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.model.Comment;
import dev.levelupschool.backend.data.repository.ArticleRepository;
import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.data.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class BackendApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
    }

    @Test
    public void givenArticle_whenGetArticles_thenReturnJsonArray() throws Exception {
        User user = new User();
        userRepository.save(user);
        var article = new Article("test title 1", "test content 1", user);

        articleRepository.save(article);

        mvc.perform(
                get("/articles")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].title", is("test title 1")));
    }

    @Test
    public void givenComment_whenGetArticle_thenReturnCommentsArray() throws Exception {
        User user = new User();
        userRepository.save(user);
        var article = articleRepository.save(new Article("test title", "test content 1", user));

        commentRepository.save(new Comment("test comment", article, user));

        mvc.perform(
                get("/articles/{id}", article.getId())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.comments", hasSize(1)));
    }
}
