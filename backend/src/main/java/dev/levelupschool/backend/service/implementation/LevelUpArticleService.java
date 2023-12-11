package dev.levelupschool.backend.service.implementation;

import dev.levelupschool.backend.data.dto.request.CreateArticleRequest;
import dev.levelupschool.backend.data.dto.request.UpdateArticleRequest;
import dev.levelupschool.backend.data.dto.response.CreateArticleResponse;
import dev.levelupschool.backend.data.model.Article;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.repository.ArticleRepository;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.exception.UserException;
import dev.levelupschool.backend.service.interfaces.ArticleService;
import dev.levelupschool.backend.service.interfaces.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class LevelUpArticleService implements ArticleService {
    private final ArticleRepository articleRepository;
    private final UserService userService;

    public LevelUpArticleService(
        ArticleRepository articleRepository,
        UserService userService){
        this.articleRepository = articleRepository;
        this.userService = userService;
    }
    @Override
    public CreateArticleResponse createArticle(CreateArticleRequest createArticleRequest, String authHeader) {
        User fondUser = userService.getUser(authHeader);
        Article newArticle = new Article(
            createArticleRequest.getTitle(),
            createArticleRequest.getContent(),
                fondUser);

        Article savedArticle = articleRepository.save(newArticle);

        CreateArticleResponse createArticleResponse = new CreateArticleResponse();
        createArticleResponse.setId(savedArticle.getId());
        createArticleResponse.setTitle(savedArticle.getTitle());
        createArticleResponse.setContent(savedArticle.getContent());
        return createArticleResponse;
    }

    @Override
    public Page<Article> findAllArticle(Pageable pageable) {
        return articleRepository
            .findAll(pageable);
    }

    @Override
    public Article findArticleById(Long articleId) {
        return articleRepository
            .findById(articleId)
            .orElseThrow(() -> new ModelNotFoundException(articleId));
    }

    @Override
    public Article updateArticle(UpdateArticleRequest updateArticleRequest, Long articleId, String authHeader) {
        User foundUser = userService.getUser(authHeader);
        return articleRepository
            .findById(articleId)
            .map(article -> {
                if (!Objects.equals(foundUser.getId(), article.getUser().getId()))throw new UserException("You have no permission to update article");
                article.setTitle(updateArticleRequest.getTitle());
                article.setContent(updateArticleRequest.getContent());
                return articleRepository.save(article);
            })
            .orElseThrow(()-> new ModelNotFoundException(articleId));
    }

    @Override
    public void deleteArticle(Long articleId, String authHeader) {
        Article foundArticle = articleRepository.findById(articleId)
            .orElseThrow(()-> new ModelNotFoundException(articleId));
        User foundUser = userService.getUser(authHeader);
        if (!Objects.equals(foundUser.getId(), foundArticle.getUser().getId()))throw new UserException("You have no permission to delete article");
        articleRepository
            .deleteById(articleId);
    }
}
