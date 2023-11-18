package dev.levelupschool.backend.service.implementation;

import dev.levelupschool.backend.data.dto.request.AddCommentRequest;
import dev.levelupschool.backend.data.dto.request.CreateArticleRequest;
import dev.levelupschool.backend.data.dto.request.UpdateCommentRequest;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.model.Comment;
import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.service.interfaces.ArticleService;
import dev.levelupschool.backend.service.interfaces.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
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
    private UserRepository userRepository;
    private CreateArticleRequest createArticleRequest;
    private AddCommentRequest addCommentRequest;

    @BeforeEach
    void setUp(){
        User user = new User();
        userRepository.save(user);
        createArticleRequest = new CreateArticleRequest();
        createArticleRequest.setTitle("Article title");
        createArticleRequest.setContent("Article content");
        createArticleRequest.setAuthorId(1L);

        addCommentRequest = new AddCommentRequest();
        addCommentRequest.setArticleId(1L);
        addCommentRequest.setContent("article comment");
        addCommentRequest.setAuthorId(1L);
    }
    @Test
    public void givenCommentRequest_whenCommentIsAdded_theRecordSizeOfCommentIncreasesByOne(){
        articleService.createArticle(createArticleRequest);
        int numberOfContentsInCommentTableBeforeAddingComment = commentService.findAllComments().size();
        assertEquals(0, numberOfContentsInCommentTableBeforeAddingComment);
        commentService.addComment(addCommentRequest);

        int numberOfCommentsInCommentTableAfterAddingAComment = commentService.findAllComments().size();
        assertEquals(1, numberOfCommentsInCommentTableAfterAddingAComment);
    }
    @Test
    public void givenIHaveAComment_whenIFindCommentById_theCommentWithThatIdIsReturned(){
        articleService.createArticle(createArticleRequest);
        commentService.addComment(addCommentRequest);
        assertNotNull(commentService.findCommentById(1L));
    }
    @Test
    public void givenAComment_whenIFindCommentWithIncorrectId_exceptionIsThrown(){
        articleService.createArticle(createArticleRequest);
        commentService.addComment(addCommentRequest);
        assertThrows(ModelNotFoundException.class, ()-> commentService.findCommentById(2L));
    }
    @Test
    public void givenIHaveComment_whenIFindAllComments_theSizeOfCommentsReturnedIsEqualToTheNumberOfCommentsIAdded(){
        int sizeOfCommentsBeforeAddingComment = commentService.findAllComments().size();
        assertEquals(0, sizeOfCommentsBeforeAddingComment);
        articleService.createArticle(createArticleRequest);
        commentService.addComment(addCommentRequest);
        int sizeOfCommentsAfterAddingComment = commentService.findAllComments().size();
        assertEquals(1, sizeOfCommentsAfterAddingComment);
    }
    @Test
    public void givenIHaveAComment_WhenIUpdateTheComment_TheCommentIsUpdatedInDBAndReturned(){
        articleService.createArticle(createArticleRequest);
        commentService.addComment(addCommentRequest);
        String contentOfCommentBeforeUpdate = commentService.findCommentById(1L).getContent();
        assertEquals("article comment", contentOfCommentBeforeUpdate);
        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest();
        updateCommentRequest.setContent("updated content");
        Comment updatedComment = commentService.updateComment(updateCommentRequest, 1L);
        assertEquals("updated content", updatedComment.getContent());
        String contentOfCommentAfterUpdatingComment = commentService.findCommentById(1L).getContent();
        assertEquals("updated content", contentOfCommentAfterUpdatingComment);
    }
    @Test
    public void givenIHaveAComment_whenIDeleteTheComment_TheSizeOfRecordInCommentTableReducesByOne(){
        articleService.createArticle(createArticleRequest);
        commentService.addComment(addCommentRequest);
        int sizeOfCommentsBeforeDeletingTheComment = commentService.findAllComments().size();
        assertEquals(1, sizeOfCommentsBeforeDeletingTheComment);
        commentService.deleteComment(1L);
        articleService.findArticleById(1L);
        int sizeOfCommentsAfterDeletingComment = commentService.findAllComments().size();
        assertEquals(0, sizeOfCommentsAfterDeletingComment);
    }
    @Test
    public void givenThereIsAnArticleWithComment_whenArticleIsDeleted_allTheArticleCommentsGetDeleted(){
        articleService.createArticle(createArticleRequest);
        commentService.addComment(addCommentRequest);
        int sizeOfCommentsBeforeDeletingArticle = commentService.findAllComments().size();
        assertEquals(1, sizeOfCommentsBeforeDeletingArticle);
        articleService.deleteArticle(1L);
        int sizeOfCommentsAfterDeletingArticle = commentService.findAllComments().size();
        assertEquals(0, sizeOfCommentsAfterDeletingArticle);
    }
}
