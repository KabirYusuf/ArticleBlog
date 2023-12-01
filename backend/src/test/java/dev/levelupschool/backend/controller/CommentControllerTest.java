package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.data.dto.request.AddCommentRequest;
import dev.levelupschool.backend.data.dto.request.AuthenticationRequest;
import dev.levelupschool.backend.data.dto.request.RegistrationRequest;
import dev.levelupschool.backend.data.dto.request.UpdateCommentRequest;
import dev.levelupschool.backend.data.dto.response.AuthenticationResponse;
import dev.levelupschool.backend.data.model.Article;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.model.Comment;
import dev.levelupschool.backend.data.repository.ArticleRepository;
import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.data.repository.CommentRepository;
import dev.levelupschool.backend.service.auth.AuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static dev.levelupschool.backend.util.Serializer.asJsonString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private AuthenticationService authenticationService;

    private RegistrationRequest registrationRequest;

    private String authHeader;
    @Test
    void contextLoads() {
    }
    @BeforeEach
    void setUp(){
        registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("kaybee");
        registrationRequest.setEmail("k@gmail.com");
        registrationRequest.setPassword("12345");
        authenticationService.register(registrationRequest);

        User foundUser = userRepository.findById(1L).get();

        foundUser.setVerified(true);

        userRepository.save(foundUser);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("kaybee");
        authenticationRequest.setPassword("12345");

        AuthenticationResponse authenticationResponse = authenticationService.login(authenticationRequest);

        authHeader = "Bearer " + authenticationResponse.getToken();

        var article = new Article("test title 1", "test content 1", userRepository.findById(1L).get(), null);
        articleRepository.save(article);

        commentRepository.save(new Comment("test comment", article, userRepository.findById(1L).get()));
    }

    @Test
    public void givenComment_whenGetArticle_thenReturnCommentsArray() throws Exception {
        User user = new User();
        user.setUsername("kabir");
        user.setPassword("123456yfg");
        user.setEmail("son@gmail.com");
        userRepository.save(user);
        var article = articleRepository.save(new Article("test title", "test content 1", user, null));

        commentRepository.save(new Comment("test comment", article, user));

        mvc.perform(
                get("/articles/1", article.getId())
                    .header("Authorization", authHeader)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.comments", hasSize(1)));
    }
    @Test
    public void givenComment_whenGetCommentWithId_thenReturnJsonOfThatSpecificComment() throws Exception {
        mvc.perform(
                get("/comments/1")
                    .header("Authorization", authHeader)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").value("test comment"));
    }

    @Test
    public void givenComment_whenDeleteCommentWithId_then200IsReturnedAsStatusCode() throws Exception {
        Assertions.assertEquals(1, commentRepository.findAll().size());
        mvc.perform(
                delete("/comments/1")
                    .header("Authorization", authHeader)
            )
            .andExpect(status().is2xxSuccessful());
        Assertions.assertEquals(0, commentRepository.findAll().size());
    }
    @Test
    public void testToGetAllCommentsInTheCommentTable() throws Exception {
        mvc.perform(
            get("/comments")
        )
            .andExpect(jsonPath("$._embedded.items.*", hasSize(1)))
            .andExpect(jsonPath("$._embedded.items[0].content").value("test comment"))
            .andExpect(status().isOk());
    }
    @Test
    public void givenAddCommentRequest_whenCommentIsAdded_CommentsInDatabaseIncreasesByOne() throws Exception {
        Assertions.assertEquals(1, commentRepository.findAll().size());

        AddCommentRequest addCommentRequest = new AddCommentRequest();
        addCommentRequest.setContent("Comment two");
        addCommentRequest.setArticleId(1L);

        mvc.perform(
            post("/comments")
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(addCommentRequest))
        )
            .andExpect(status().isCreated());
        Assertions.assertEquals(2, commentRepository.findAll().size());
    }
    @Test
    public void testThatCommentCanBeUpdated() throws Exception {
        Assertions.assertEquals("test comment", commentRepository.findById(1L).get().getContent());

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest();
        updateCommentRequest.setContent("Updated Comment");

        mvc.perform(
            put("/comments/1")
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updateCommentRequest))
        )
            .andExpect(status().isOk());
        Assertions.assertEquals("Updated Comment", commentRepository.findById(1L).get().getContent());
    }

    @Test
    public void testThatAUserCannotUpdateCommentCreatedByAnotherUser() throws Exception {
        RegistrationRequest registrationRequestForSecondUser = new RegistrationRequest();
        registrationRequestForSecondUser.setEmail("kabir@gmail.com");
        registrationRequestForSecondUser.setUsername("kaybeeTwo");
        registrationRequestForSecondUser.setPassword("12345");
        authenticationService.register(registrationRequestForSecondUser);

        AuthenticationRequest authenticationRequestForSecondUser = new AuthenticationRequest();
        authenticationRequestForSecondUser.setUsername("kaybeeTwo");
        authenticationRequestForSecondUser.setPassword("12345");

        User foundUser = userRepository.findById(2L).get();

        foundUser.setVerified(true);

        userRepository.save(foundUser);

        AuthenticationResponse authenticationResponse = authenticationService.login(authenticationRequestForSecondUser);

        String authHeaderForSecondUser = "Bearer " + authenticationResponse.getToken();

        Assertions.assertEquals("test comment", commentRepository.findById(1L).get().getContent());

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest();
        updateCommentRequest.setContent("Updated Comment");

        mvc.perform(
                put("/comments/1")
                    .header("Authorization", authHeaderForSecondUser)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(updateCommentRequest))
            )
            .andExpect(status().is4xxClientError());
        Assertions.assertEquals("test comment", commentRepository.findById(1L).get().getContent());
    }

    @Test
    public void testThatAUserCannotDeleteCommentCreatedByAnotherUser() throws Exception {
        RegistrationRequest registrationRequestForSecondUser = new RegistrationRequest();
        registrationRequestForSecondUser.setEmail("kabir@gmail.com");
        registrationRequestForSecondUser.setUsername("kaybeeTwo");
        registrationRequestForSecondUser.setPassword("12345");
        authenticationService.register(registrationRequestForSecondUser);

        AuthenticationRequest authenticationRequestForSecondUser = new AuthenticationRequest();
        authenticationRequestForSecondUser.setUsername("kaybeeTwo");
        authenticationRequestForSecondUser.setPassword("12345");

        User foundUser = userRepository.findById(2L).get();

        foundUser.setVerified(true);

        userRepository.save(foundUser);

        AuthenticationResponse authenticationResponse = authenticationService.login(authenticationRequestForSecondUser);

        String authHeaderForSecondUser = "Bearer " + authenticationResponse.getToken();

        Assertions.assertEquals(1, commentRepository.findAll().size());
        mvc.perform(
                delete("/comments/1")
                    .header("Authorization", authHeaderForSecondUser)
            )
            .andExpect(status().is4xxClientError());
        Assertions.assertEquals(1, commentRepository.findAll().size());
    }

}
