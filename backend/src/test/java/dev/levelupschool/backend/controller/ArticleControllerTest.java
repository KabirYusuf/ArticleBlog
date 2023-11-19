//package dev.levelupschool.backend.controller;
//
//import dev.levelupschool.backend.data.dto.request.CreateArticleRequest;
//import dev.levelupschool.backend.data.dto.request.UpdateArticleRequest;
//import dev.levelupschool.backend.data.model.Article;
//import dev.levelupschool.backend.data.model.User;
//import dev.levelupschool.backend.data.repository.ArticleRepository;
//import dev.levelupschool.backend.data.repository.UserRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static dev.levelupschool.backend.util.Serializer.asJsonString;
//import static org.hamcrest.Matchers.hasSize;
//import static org.hamcrest.Matchers.is;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@AutoConfigureMockMvc
//@SpringBootTest
//@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//class ArticleControllerTest {
//
//    @Autowired
//    private MockMvc mvc;
//
//    @Autowired
//    private ArticleRepository articleRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//    @Test
//    void contextLoads() {
//    }
//    @BeforeEach
//    void setUp(){
//        User user = new User();
//        userRepository.save(user);
//        var article = new Article("test title 1", "test content 1", user);
//
//        articleRepository.save(article);
//    }
//
//    @Test
//    public void givenArticle_whenGetArticles_thenReturnJsonArray() throws Exception {
//        mvc.perform(
//                get("/articles")
//                    .contentType(MediaType.APPLICATION_JSON))
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$", hasSize(1)))
//            .andExpect(jsonPath("$[0].title", is("test title 1")))
//            .andExpect(jsonPath("$[0].author.id", is(1)));
//    }
//
//    @Test
//    public void givenArticle_whenGetArticlesWithId_thenReturnJsonOfThatSpecificArticle() throws Exception {
//        mvc.perform(
//            get("/articles/1")
//        )
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.title").value("test title 1"))
//            .andExpect(jsonPath("$.content").value("test content 1"));
//    }
//
//    @Test
//    public void givenArticle_whenDeleteArticleWithId_then200IsReturnedAsStatusCode() throws Exception {
//        Assertions.assertEquals(1, articleRepository.findAll().size());
//        mvc.perform(
//                delete("/articles/1")
//            )
//            .andExpect(status().is2xxSuccessful());
//
//        Assertions.assertEquals(0, articleRepository.findAll().size());
//    }
//    @Test
//    public void givenAnArticle_whenArticleIsUpdated_theArticleIsUpdatedAndTheNewArticleIsReturned() throws Exception {
//        Assertions.assertEquals("test title 1", articleRepository.findById(1L).get().getTitle());
//        Assertions.assertEquals("test content 1", articleRepository.findById(1L).get().getContent());
//
//        UpdateArticleRequest updateArticleRequest = new UpdateArticleRequest();
//        updateArticleRequest.setTitle("updated title");
//        updateArticleRequest.setContent("updated content");
//
//        mvc.perform(
//            put("/articles/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(updateArticleRequest))
//        )
//            .andExpect(jsonPath("$.title").value("updated title"))
//            .andExpect(jsonPath("$.content").value("updated content"));
//
//        Assertions.assertEquals("updated title", articleRepository.findById(1L).get().getTitle());
//        Assertions.assertEquals("updated content", articleRepository.findById(1L).get().getContent());
//    }
//
//    @Test
//    public void givenACreateArticleRequest_whenArticleIsCreated_theNumberOfArticlesIncreasesByOne() throws Exception {
//        Assertions.assertEquals(1, articleRepository.findAll().size());
//
//        CreateArticleRequest createArticleRequest = new CreateArticleRequest();
//        createArticleRequest.setAuthorId(1L);
//        createArticleRequest.setTitle("Second article title");
//        createArticleRequest.setContent("Second article content");
//
//        mvc.perform(
//            post("/articles")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(createArticleRequest))
//        )
//            .andExpect(status().isCreated());
//
//        Assertions.assertEquals(2, articleRepository.findAll().size());
//    }
//
//}
