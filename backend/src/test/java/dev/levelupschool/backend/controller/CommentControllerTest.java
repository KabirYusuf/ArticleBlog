//package dev.levelupschool.backend.controller;
//
//import dev.levelupschool.backend.data.dto.request.AddCommentRequest;
//import dev.levelupschool.backend.data.dto.request.UpdateCommentRequest;
//import dev.levelupschool.backend.data.model.Article;
//import dev.levelupschool.backend.data.model.User;
//import dev.levelupschool.backend.data.model.Comment;
//import dev.levelupschool.backend.data.repository.ArticleRepository;
//import dev.levelupschool.backend.data.repository.UserRepository;
//import dev.levelupschool.backend.data.repository.CommentRepository;
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
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@AutoConfigureMockMvc
//@SpringBootTest
//@ActiveProfiles("test")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//class CommentControllerTest {
//    @Autowired
//    private MockMvc mvc;
//
//    @Autowired
//    private ArticleRepository articleRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private CommentRepository commentRepository;
//    @Test
//    void contextLoads() {
//    }
//    @BeforeEach
//    void setUp(){
//        User user = new User();
//        userRepository.save(user);
//        var article = new Article("test title 1", "test content 1", user);
//        articleRepository.save(article);
//
//        commentRepository.save(new Comment("test comment", article, user));
//    }
//
//    @Test
//    public void givenComment_whenGetArticle_thenReturnCommentsArray() throws Exception {
//        User user = new User();
//        userRepository.save(user);
//        var article = articleRepository.save(new Article("test title", "test content 1", user));
//
//        commentRepository.save(new Comment("test comment", article, user));
//
//        mvc.perform(
//                get("/articles/{id}", article.getId())
//                    .contentType(MediaType.APPLICATION_JSON)
//            )
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.comments", hasSize(1)));
//    }
//    @Test
//    public void givenComment_whenGetCommentWithId_thenReturnJsonOfThatSpecificComment() throws Exception {
//        mvc.perform(
//                get("/comments/1")
//            )
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.content").value("test comment"));
//    }
//
//    @Test
//    public void givenComment_whenDeleteCommentWithId_then200IsReturnedAsStatusCode() throws Exception {
//        Assertions.assertEquals(1, commentRepository.findAll().size());
//        mvc.perform(
//                delete("/comments/1")
//            )
//            .andExpect(status().is2xxSuccessful());
//        Assertions.assertEquals(0, commentRepository.findAll().size());
//    }
//    @Test
//    public void testToGetAllCommentsInTheCommentTable() throws Exception {
//        mvc.perform(
//            get("/comments")
//        )
//            .andExpect(jsonPath("$.*", hasSize(1)))
//            .andExpect(jsonPath("$[0].content").value("test comment"))
//            .andExpect(status().isOk());
//    }
//    @Test
//    public void givenAddCommentRequest_whenCommentIsAdded_CommentsInDatabaseIncreasesByOne() throws Exception {
//        Assertions.assertEquals(1, commentRepository.findAll().size());
//
//        AddCommentRequest addCommentRequest = new AddCommentRequest();
//        addCommentRequest.setContent("Comment two");
//        addCommentRequest.setAuthorId(1L);
//        addCommentRequest.setArticleId(1L);
//
//        mvc.perform(
//            post("/comments")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(addCommentRequest))
//        )
//            .andExpect(status().isCreated());
//        Assertions.assertEquals(2, commentRepository.findAll().size());
//    }
//    @Test
//    public void testThatCommentCanBeUpdated() throws Exception {
//        Assertions.assertEquals("test comment", commentRepository.findById(1L).get().getContent());
//
//        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest();
//        updateCommentRequest.setContent("Updated Comment");
//
//        mvc.perform(
//            put("/comments/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(updateCommentRequest))
//        )
//            .andExpect(status().isOk());
//        Assertions.assertEquals("Updated Comment", commentRepository.findById(1L).get().getContent());
//    }
//
//}
