package dev.levelupschool.backend.service.interfaces;

import dev.levelupschool.backend.data.dto.request.CreateArticleRequest;
import dev.levelupschool.backend.data.dto.request.UpdateArticleRequest;
import dev.levelupschool.backend.data.dto.response.CreateArticleResponse;
import dev.levelupschool.backend.data.model.Article;

import java.util.List;

public interface ArticleService {
    CreateArticleResponse createArticle(CreateArticleRequest createArticleRequest);
    List<Article> findAllArticle();

    Article findArticleById(Long articleId);

    Article updateArticle(UpdateArticleRequest updateArticleRequest, Long articleId);

    void deleteArticle(Long articleId);
}
