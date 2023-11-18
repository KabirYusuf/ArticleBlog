package dev.levelupschool.backend.service.implementation;

import dev.levelupschool.backend.data.dto.request.CreateArticleRequest;
import dev.levelupschool.backend.data.dto.request.UpdateArticleRequest;
import dev.levelupschool.backend.data.model.Article;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.service.interfaces.ArticleService;
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
    private UserRepository userRepository;
    private CreateArticleRequest createArticleRequest;

    @BeforeEach
    void setup(){
        User user = new User();
        userRepository.save(user);
        createArticleRequest = new CreateArticleRequest();
        createArticleRequest.setTitle("Article title");
        createArticleRequest.setContent("Article content");
        createArticleRequest.setAuthorId(1L);
    }
    @Test
    public void givenCreateArticleRequest_whenArticleSave_articleTableRecordsIncreasesByOne(){
        int numberOfArticlesBeforeCreatingAnArticle = articleService.findAllArticle().size();

        assertEquals(0, numberOfArticlesBeforeCreatingAnArticle);

        articleService.createArticle(createArticleRequest);

        int numberOfArticlesAfterCreatingNewArticle = articleService.findAllArticle().size();

        assertEquals(1, numberOfArticlesAfterCreatingNewArticle);
    }

    @Test
    public void givenAnArticle_whenIFindArticleById_articleWithThatIdIsReturned(){
        articleService.createArticle(createArticleRequest);

        Article foundArticle = articleService.findArticleById(1L);

        assertNotNull(foundArticle);
    }

    @Test
    public void givenAnArticle_whenIFindArticleWithIncorrectId_exceptionIsThrown(){
        articleService.createArticle(createArticleRequest);
        assertThrows(ModelNotFoundException.class, ()-> articleService.findArticleById(3L));
    }
    @Test
    public void givenIHaveAnArticle_whenArticleIsUpdated_theUpdatedArticleIsSavedAndReturned(){
        articleService.createArticle(createArticleRequest);

        String articleTitleBeforeUpdate = "Article title";
        assertEquals(articleTitleBeforeUpdate, articleService.findArticleById(1L).getTitle());
        UpdateArticleRequest updateArticleRequest = new UpdateArticleRequest();

        updateArticleRequest.setContent("Updated article content");
        updateArticleRequest.setTitle("updated article title");

        Article updatedArticle = articleService.updateArticle(updateArticleRequest, 1L);
        assertEquals("updated article title", updatedArticle.getTitle());
        String articleTitleAfterUpdate = "updated article title";
        assertEquals(articleTitleAfterUpdate, articleService.findArticleById(1L).getTitle());

    }

    @Test
    public void givenThereAnArticleWithId1_whenArticleIsDeleted_theSizeOfArticleTableRecodeReducesByOne(){
        articleService.createArticle(createArticleRequest);
        int numberOfArticlesBeforeDeletingAnArticle = articleService.findAllArticle().size();
        assertEquals(1, numberOfArticlesBeforeDeletingAnArticle);

        articleService.deleteArticle(1L);

        int numberOfArticlesAfterDeletingAnArticle = articleService.findAllArticle().size();
        assertEquals(0, numberOfArticlesAfterDeletingAnArticle);
    }
}
