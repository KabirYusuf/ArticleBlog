package dev.levelupschool.backend.service.implementation;

import dev.levelupschool.backend.data.dto.request.*;
import dev.levelupschool.backend.data.dto.response.AuthenticationResponse;
import dev.levelupschool.backend.data.model.Comment;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.repository.CommentRepository;
import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.exception.UserException;
import dev.levelupschool.backend.service.auth.AuthenticationService;
import dev.levelupschool.backend.service.interfaces.ArticleService;
import dev.levelupschool.backend.service.interfaces.CommentService;
import dev.levelupschool.backend.service.notification.LevelUpEmailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LevelUpCommentServiceTest {
    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;
    @MockBean
    private LevelUpEmailSenderService levelUpEmailSenderService;
    private CreateArticleRequest createArticleRequest;
    private AddCommentRequest addCommentRequest;

    private RegistrationRequest registrationRequest;

    private String authHeader;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
        doNothing().when(levelUpEmailSenderService).sendEmailNotification(any(NotificationRequest.class));
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


        addCommentRequest = new AddCommentRequest();
        addCommentRequest.setArticleId(1L);
        addCommentRequest.setContent("article comment");

    }
    @Test
    public void givenCommentRequest_whenCommentIsAdded_theRecordSizeOfCommentIncreasesByOne(){
        articleService.createArticle(createArticleRequest, authHeader);
        int numberOfContentsInCommentTableBeforeAddingComment = commentRepository.findAll().size();
        assertEquals(0, numberOfContentsInCommentTableBeforeAddingComment);
        commentService.addComment(addCommentRequest, authHeader);

        int numberOfCommentsInCommentTableAfterAddingAComment = commentRepository.findAll().size();
        assertEquals(1, numberOfCommentsInCommentTableAfterAddingAComment);
    }
    @Test
    public void givenIHaveAComment_whenIFindCommentById_theCommentWithThatIdIsReturned(){
        articleService.createArticle(createArticleRequest, authHeader);
        commentService.addComment(addCommentRequest, authHeader);
        assertNotNull(commentService.findCommentById(1L));
    }
    @Test
    public void givenAComment_whenIFindCommentWithIncorrectId_exceptionIsThrown(){
        articleService.createArticle(createArticleRequest, authHeader);
        commentService.addComment(addCommentRequest, authHeader);
        assertThrows(ModelNotFoundException.class, ()-> commentService.findCommentById(2L));
    }
    @Test
    public void givenIHaveComment_whenIFindAllComments_theSizeOfCommentsReturnedIsEqualToTheNumberOfCommentsIAdded(){
        int sizeOfCommentsBeforeAddingComment = commentRepository.findAll().size();
        assertEquals(0, sizeOfCommentsBeforeAddingComment);
        articleService.createArticle(createArticleRequest, authHeader);
        commentService.addComment(addCommentRequest, authHeader);
        int sizeOfCommentsAfterAddingComment = commentRepository.findAll().size();
        assertEquals(1, sizeOfCommentsAfterAddingComment);
    }
    @Test
    public void givenIHaveAComment_WhenIUpdateTheComment_TheCommentIsUpdatedInDBAndReturned(){
        articleService.createArticle(createArticleRequest, authHeader);
        commentService.addComment(addCommentRequest, authHeader);
        String contentOfCommentBeforeUpdate = commentService.findCommentById(1L).getContent();
        assertEquals("article comment", contentOfCommentBeforeUpdate);
        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest();
        updateCommentRequest.setContent("updated content");
        Comment updatedComment = commentService.updateComment(updateCommentRequest, 1L, authHeader);
        assertEquals("updated content", updatedComment.getContent());
        String contentOfCommentAfterUpdatingComment = commentService.findCommentById(1L).getContent();
        assertEquals("updated content", contentOfCommentAfterUpdatingComment);
    }
    @Test
    public void givenIHaveAComment_whenIDeleteTheComment_TheSizeOfRecordInCommentTableReducesByOne(){
        articleService.createArticle(createArticleRequest, authHeader);
        commentService.addComment(addCommentRequest, authHeader);
        int sizeOfCommentsBeforeDeletingTheComment = commentRepository.findAll().size();
        assertEquals(1, sizeOfCommentsBeforeDeletingTheComment);
        commentService.deleteComment(1L, authHeader);
        articleService.findArticleById(1L);
        int sizeOfCommentsAfterDeletingComment = commentRepository.findAll().size();
        assertEquals(0, sizeOfCommentsAfterDeletingComment);
    }
    @Test
    public void givenThereIsAnArticleWithComment_whenArticleIsDeleted_allTheArticleCommentsGetDeleted(){
        articleService.createArticle(createArticleRequest, authHeader);
        commentService.addComment(addCommentRequest, authHeader);
        int sizeOfCommentsBeforeDeletingArticle = commentRepository.findAll().size();
        assertEquals(1, sizeOfCommentsBeforeDeletingArticle);
        articleService.deleteArticle(1L, authHeader);
        int sizeOfCommentsAfterDeletingArticle = commentRepository.findAll().size();
        assertEquals(0, sizeOfCommentsAfterDeletingArticle);
    }

    @Test
    public void testThatAUserCannotDeleteCommentMadeByAnotherUser(){
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

        articleService.createArticle(createArticleRequest, authHeader);
        commentService.addComment(addCommentRequest, authHeader);
        int sizeOfCommentsBeforeDeletingTheComment = commentRepository.findAll().size();
        assertEquals(1, sizeOfCommentsBeforeDeletingTheComment);
        assertThrows(UserException.class, ()-> commentService.deleteComment(1L, authHeaderForSecondUser));

        int sizeOfCommentsAfterDeletingComment = commentRepository.findAll().size();
        assertEquals(1, sizeOfCommentsAfterDeletingComment);
    }

    @Test
    public void testThatAUserCannotUpdateCommentPostedByAnotherUser(){
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
        commentService.addComment(addCommentRequest, authHeader);

        String contentOfCommentBeforeUpdate = commentService.findCommentById(1L).getContent();
        assertEquals("article comment", contentOfCommentBeforeUpdate);

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest();
        updateCommentRequest.setContent("updated content");

        assertThrows(UserException.class, ()-> commentService.updateComment(updateCommentRequest, 1L, authHeaderForSecondUser));
        assertEquals("article comment", commentRepository.findById(1L).get().getContent());

    }
}
