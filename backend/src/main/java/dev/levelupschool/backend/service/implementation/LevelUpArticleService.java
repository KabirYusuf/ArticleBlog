package dev.levelupschool.backend.service.implementation;

import dev.levelupschool.backend.data.dto.request.CreateArticleRequest;
import dev.levelupschool.backend.data.dto.request.UpdateArticleRequest;
import dev.levelupschool.backend.data.dto.response.CreateArticleResponse;
import dev.levelupschool.backend.data.model.Article;
import dev.levelupschool.backend.data.model.Author;
import dev.levelupschool.backend.data.repository.ArticleRepository;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.service.interfaces.ArticleService;
import dev.levelupschool.backend.service.interfaces.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelUpArticleService implements ArticleService {
    private final ArticleRepository articleRepository;
    private final AuthorService authorService;
    public LevelUpArticleService(
        ArticleRepository articleRepository,
        AuthorService authorService){
        this.articleRepository = articleRepository;
        this.authorService = authorService;
    }
    @Override
    public CreateArticleResponse createArticle(CreateArticleRequest createArticleRequest) {
        Author fondAuthor = authorService.findAuthorById(createArticleRequest.getAuthorId());
        Article newArticle = new Article(
            createArticleRequest.getTitle(),
            createArticleRequest.getContent(),
            fondAuthor);

        Article savedArticle = articleRepository.save(newArticle);

        CreateArticleResponse createArticleResponse = new CreateArticleResponse();
        createArticleResponse.setId(savedArticle.getId());
        createArticleResponse.setTitle(savedArticle.getTitle());
        createArticleResponse.setContent(savedArticle.getContent());
        return createArticleResponse;
    }

    @Override
    public List<Article> findAllArticle() {
        return articleRepository
            .findAll();
    }

    @Override
    public Article findArticleById(Long articleId) {
        return articleRepository
            .findById(articleId)
            .orElseThrow(() -> new ModelNotFoundException(articleId));
    }

    @Override
    public Article updateArticle(UpdateArticleRequest updateArticleRequest, Long articleId) {
        return articleRepository
            .findById(articleId)
            .map(article -> {
                article.setTitle(updateArticleRequest.getTitle());
                article.setContent(updateArticleRequest.getContent());
                return articleRepository.save(article);
            })
            .orElseThrow(()-> new ModelNotFoundException(articleId));
    }

    @Override
    public void deleteArticle(Long articleId) {
        articleRepository
            .deleteById(articleId);
    }
}
