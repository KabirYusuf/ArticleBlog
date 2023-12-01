package dev.levelupschool.backend.service.interfaces;

import dev.levelupschool.backend.data.dto.request.CreateArticleRequest;
import dev.levelupschool.backend.data.dto.request.UpdateArticleRequest;
import dev.levelupschool.backend.data.dto.response.CreateArticleResponse;
import dev.levelupschool.backend.data.model.Article;
import dev.levelupschool.backend.data.model.enums.ReactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleService {
    CreateArticleResponse createArticle(CreateArticleRequest createArticleRequest, String authHeader);
    Page<Article> findAllArticle(Pageable pageable);

    Article findArticleById(Long articleId);

    Article updateArticle(UpdateArticleRequest updateArticleRequest, Long articleId, String authHeader);

    void deleteArticle(Long articleId, String authHeader);
    void reactToArticle(Long articleId, String reactionType, String authHeader);
    void removeReaction(Long articleId, String authHeader);
}
