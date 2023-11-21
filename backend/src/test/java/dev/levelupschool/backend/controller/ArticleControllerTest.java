package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.data.dto.request.AuthenticationRequest;
import dev.levelupschool.backend.data.dto.request.CreateArticleRequest;
import dev.levelupschool.backend.data.dto.request.RegistrationRequest;
import dev.levelupschool.backend.data.dto.request.UpdateArticleRequest;
import dev.levelupschool.backend.data.dto.response.AuthenticationResponse;
import dev.levelupschool.backend.data.model.Article;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.repository.ArticleRepository;
import dev.levelupschool.backend.data.repository.UserRepository;
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
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private AuthenticationService authenticationService;
    private String authHeader;
    private RegistrationRequest registrationRequest;


    @Autowired
    private UserRepository userRepository;
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

        CreateArticleRequest createArticleRequest = new CreateArticleRequest();
        createArticleRequest.setTitle("Article title");
        createArticleRequest.setContent("Article content");



        var article = new Article("test title 1", "test content 1", userRepository.findById(1L).get());

        articleRepository.save(article);
    }

    @Test
    public void givenArticle_whenGetArticles_thenReturnJsonArray() throws Exception {
        mvc.perform(get("/articles")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.items", hasSize(1)))
            .andExpect(jsonPath("$._embedded.items[0].title", is("test title 1")))
            .andExpect(jsonPath("$._embedded.items[0].user.id", is(1)))

            .andExpect(jsonPath("$._links.self.href", is(notNullValue())))
            .andExpect(jsonPath("$.page.size", is(20)))
            .andExpect(jsonPath("$.page.totalElements", is(notNullValue())))
            .andExpect(jsonPath("$.page.totalPages", is(notNullValue())))
            .andExpect(jsonPath("$.page.number", is(0)));
    }

    @Test
    public void givenArticle_whenGetArticlesWithId_thenReturnJsonOfThatSpecificArticle() throws Exception {
        mvc.perform(
            get("/articles/1")
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("test title 1"))
            .andExpect(jsonPath("$.content").value("test content 1"));
    }

    @Test
    public void givenArticle_whenDeleteArticleWithId_then200IsReturnedAsStatusCode() throws Exception {
        Assertions.assertEquals(1, articleRepository.findAll().size());
        mvc.perform(
                delete("/articles/1")
                    .header("Authorization", authHeader)
            )
            .andExpect(status().is2xxSuccessful());

        Assertions.assertEquals(0, articleRepository.findAll().size());
    }
    @Test
    public void givenAnArticle_whenArticleIsUpdated_theArticleIsUpdatedAndTheNewArticleIsReturned() throws Exception {
        Assertions.assertEquals("test title 1", articleRepository.findById(1L).get().getTitle());
        Assertions.assertEquals("test content 1", articleRepository.findById(1L).get().getContent());

        UpdateArticleRequest updateArticleRequest = new UpdateArticleRequest();
        updateArticleRequest.setTitle("updated title");
        updateArticleRequest.setContent("updated content");

        mvc.perform(
            put("/articles/1")
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updateArticleRequest))
        )
            .andExpect(jsonPath("$.title").value("updated title"))
            .andExpect(jsonPath("$.content").value("updated content"));

        Assertions.assertEquals("updated title", articleRepository.findById(1L).get().getTitle());
        Assertions.assertEquals("updated content", articleRepository.findById(1L).get().getContent());
    }

    @Test
    public void givenACreateArticleRequest_whenArticleIsCreated_theNumberOfArticlesIncreasesByOne() throws Exception {
        Assertions.assertEquals(1, articleRepository.findAll().size());

        CreateArticleRequest createArticleRequest = new CreateArticleRequest();
        createArticleRequest.setTitle("Second article title");
        createArticleRequest.setContent("Second article content");

        mvc.perform(
            post("/articles")
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(createArticleRequest))
        )
            .andExpect(status().isCreated());

        Assertions.assertEquals(2, articleRepository.findAll().size());
    }

    @Test
    public void testThatAUserCannotUpdateArticleCreatedByAnotherUser() throws Exception {
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


        Assertions.assertEquals("test title 1", articleRepository.findById(1L).get().getTitle());
        Assertions.assertEquals("test content 1", articleRepository.findById(1L).get().getContent());

        UpdateArticleRequest updateArticleRequest = new UpdateArticleRequest();
        updateArticleRequest.setTitle("updated title");
        updateArticleRequest.setContent("updated content");

        mvc.perform(
                put("/articles/1")
                    .header("Authorization", authHeaderForSecondUser)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(updateArticleRequest))
            )
                .andExpect(status().is4xxClientError());

        Assertions.assertEquals("test title 1", articleRepository.findById(1L).get().getTitle());
        Assertions.assertEquals("test content 1", articleRepository.findById(1L).get().getContent());
    }

    @Test
    public void testThatUserCannotDeleteArticleCreatedByAnotherUser() throws Exception {
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

        Assertions.assertEquals(1, articleRepository.findAll().size());
        mvc.perform(
                delete("/articles/1")
                    .header("Authorization", authHeaderForSecondUser)
            )
            .andExpect(status().is4xxClientError());

        Assertions.assertEquals(1, articleRepository.findAll().size());
    }

}
