package dev.levelupschool.backend.service.implementation;

import dev.levelupschool.backend.data.dto.request.AuthenticationRequest;
import dev.levelupschool.backend.data.dto.request.CreateArticleRequest;
import dev.levelupschool.backend.data.dto.request.RegistrationRequest;
import dev.levelupschool.backend.data.dto.request.UpdateArticleRequest;
import dev.levelupschool.backend.data.dto.response.AuthenticationResponse;
import dev.levelupschool.backend.data.model.Article;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.model.UserArticleReaction;
import dev.levelupschool.backend.data.model.enums.ReactionType;
import dev.levelupschool.backend.data.repository.ArticleRepository;
import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.exception.ReactionException;
import dev.levelupschool.backend.exception.UserException;
import dev.levelupschool.backend.service.auth.AuthenticationService;
import dev.levelupschool.backend.service.interfaces.ArticleService;
import dev.levelupschool.backend.service.interfaces.UserArticleReactionService;
import dev.levelupschool.backend.util.UserArticleReactionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LevelUpArticleServiceTest {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private UserArticleReactionService userArticleReactionService;

    private CreateArticleRequest createArticleRequest;
    private String authHeader;

    private RegistrationRequest registrationRequest;

    @BeforeEach
    void setup(){
        registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail("kabir@gmail.com");
        registrationRequest.setUsername("kaybee");
        registrationRequest.setPassword("12345");

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("kaybee");
        authenticationRequest.setPassword("12345");

        authenticationService.register(registrationRequest);

        User foundUser = userRepository.findById(1L).get();

        foundUser.setVerified(true);

        userRepository.save(foundUser);

        AuthenticationResponse authenticationResponse = authenticationService.login(authenticationRequest);

        authHeader = "Bearer " + authenticationResponse.getToken();

        createArticleRequest = new CreateArticleRequest();
        createArticleRequest.setTitle("Article title");
        createArticleRequest.setContent("Article content");
    }
    @Test
    public void givenCreateArticleRequest_whenArticleSave_articleTableRecordsIncreasesByOne(){
        int numberOfArticlesBeforeCreatingAnArticle = articleRepository.findAll().size();

        assertEquals(0, numberOfArticlesBeforeCreatingAnArticle);

        articleService.createArticle(createArticleRequest, authHeader);

        int numberOfArticlesAfterCreatingNewArticle = articleRepository.findAll().size();

        assertEquals(1, numberOfArticlesAfterCreatingNewArticle);
    }

    @Test
    public void givenAnArticle_whenIFindArticleById_articleWithThatIdIsReturned(){
        articleService.createArticle(createArticleRequest, authHeader);

        Article foundArticle = articleService.findArticleById(1L);

        assertNotNull(foundArticle);
    }

    @Test
    public void givenAnArticle_whenIFindArticleWithIncorrectId_exceptionIsThrown(){
        articleService.createArticle(createArticleRequest, authHeader);
        assertThrows(ModelNotFoundException.class, ()-> articleService.findArticleById(3L));
    }
    @Test
    public void givenIHaveAnArticle_whenArticleIsUpdated_theUpdatedArticleIsSavedAndReturned(){
        articleService.createArticle(createArticleRequest, authHeader);

        String articleTitleBeforeUpdate = "Article title";
        assertEquals(articleTitleBeforeUpdate, articleService.findArticleById(1L).getTitle());
        UpdateArticleRequest updateArticleRequest = new UpdateArticleRequest();

        updateArticleRequest.setContent("Updated article content");
        updateArticleRequest.setTitle("updated article title");

        Article updatedArticle = articleService.updateArticle(updateArticleRequest, 1L, authHeader);
        assertEquals("updated article title", updatedArticle.getTitle());
        String articleTitleAfterUpdate = "updated article title";
        assertEquals(articleTitleAfterUpdate, articleService.findArticleById(1L).getTitle());

    }

    @Test
    public void givenThereAnArticleWithId1_whenArticleIsDeleted_theSizeOfArticleTableRecodeReducesByOne(){
        articleService.createArticle(createArticleRequest, authHeader);
        int numberOfArticlesBeforeDeletingAnArticle = articleRepository.findAll().size();
        assertEquals(1, numberOfArticlesBeforeDeletingAnArticle);

        articleService.deleteArticle(1L, authHeader);

        int numberOfArticlesAfterDeletingAnArticle = articleRepository.findAll().size();
        assertEquals(0, numberOfArticlesAfterDeletingAnArticle);
    }
    @Test
    public void testThatAUserCannotDeleteArticleCreatedByAnotherUser(){
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

        articleService.createArticle(createArticleRequest, authHeader);
        int numberOfArticlesBeforeDeletingAnArticle = articleRepository.findAll().size();
        assertEquals(1, numberOfArticlesBeforeDeletingAnArticle);

        assertThrows(UserException.class, ()-> articleService.deleteArticle(1L, authHeaderForSecondUser));

        int numberOfArticlesAfterDeletingAnArticle = articleRepository.findAll().size();
        assertEquals(1, numberOfArticlesAfterDeletingAnArticle);
    }

    @Test
    public void testThatUserCannotUpdateArticleCreatedByAnotherUser(){
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

        articleService.createArticle(createArticleRequest, authHeader);

        String articleTitleBeforeUpdate = "Article title";
        assertEquals(articleTitleBeforeUpdate, articleService.findArticleById(1L).getTitle());

        UpdateArticleRequest updateArticleRequest = new UpdateArticleRequest();
        updateArticleRequest.setContent("Updated article content");
        updateArticleRequest.setTitle("updated article title");

        assertThrows(UserException.class, ()-> articleService.updateArticle(updateArticleRequest, 1L, authHeaderForSecondUser));
        assertEquals(articleTitleBeforeUpdate, articleService.findArticleById(1L).getTitle());
    }

    @Test
    public void givenArticleAndReaction_whenUserReactsToArticle_thenReactionIsSaved() {
        articleService.createArticle(createArticleRequest, authHeader);
        Article createdArticle = articleRepository.findAll().get(0);

        assertDoesNotThrow(() -> articleService.reactToArticle(createdArticle.getId(), "LIKE", authHeader));


        UserArticleReaction reaction = userArticleReactionService.findUserArticleReactionById(new UserArticleReactionId(1L, createdArticle.getId()));
        assertNotNull(reaction);
        assertEquals(ReactionType.LIKE, reaction.getReaction());
    }

    @Test
    public void givenExistingReaction_whenUserUpdatesReaction_thenReactionIsUpdated() {
        articleService.createArticle(createArticleRequest, authHeader);
        Article createdArticle = articleRepository.findAll().get(0);

        articleService.reactToArticle(createdArticle.getId(), "LIKE", authHeader);

        assertDoesNotThrow(() -> articleService.reactToArticle(createdArticle.getId(), "LOVE", authHeader));


        UserArticleReaction updatedReaction = userArticleReactionService.findUserArticleReactionById(new UserArticleReactionId(1L, createdArticle.getId()));
        assertNotNull(updatedReaction);
        assertEquals(ReactionType.LOVE, updatedReaction.getReaction());
    }

    @Test
    public void givenArticleAndReaction_whenUserRemovesReaction_thenReactionIsRemoved() {
        articleService.createArticle(createArticleRequest, authHeader);
        Article createdArticle = articleRepository.findAll().get(0);

        articleService.reactToArticle(createdArticle.getId(), "LIKE", authHeader);

        assertDoesNotThrow(() -> articleService.removeReaction(createdArticle.getId(), authHeader));


        UserArticleReaction removedReaction = userArticleReactionService.findUserArticleReactionById(new UserArticleReactionId(1L, createdArticle.getId()));
        assertNull(removedReaction);
    }

    @Test
    public void givenNoExistingReaction_whenRemoveReactionIsCalled_thenExceptionIsThrown() {
        articleService.createArticle(createArticleRequest, authHeader);
        Article createdArticle = articleRepository.findAll().get(0);

        assertThrows(ReactionException.class, () -> articleService.removeReaction(createdArticle.getId(), authHeader));
    }
}
