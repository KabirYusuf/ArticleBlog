package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.data.dto.request.CreateArticleRequest;
import dev.levelupschool.backend.data.dto.request.UpdateArticleRequest;
import dev.levelupschool.backend.data.dto.response.CreateArticleResponse;
import dev.levelupschool.backend.data.repository.ArticleRepository;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.data.model.Article;
import dev.levelupschool.backend.service.interfaces.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<List<Article>> index() {
        return ResponseEntity.ok(articleService.findAllArticle());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> show(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.findArticleById(id));
    }

    @PostMapping
    public ResponseEntity<CreateArticleResponse> store(@RequestBody CreateArticleRequest createArticleRequest) {
        return new ResponseEntity<>(articleService.createArticle(createArticleRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> update(@RequestBody UpdateArticleRequest updateArticleRequest, @PathVariable Long id) {
        return ResponseEntity.ok(articleService.updateArticle(updateArticleRequest, id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        articleService.deleteArticle(id);
    }
}
