package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.data.dto.request.CreateArticleRequest;
import dev.levelupschool.backend.data.dto.request.UpdateArticleRequest;
import dev.levelupschool.backend.data.dto.response.CreateArticleResponse;
import dev.levelupschool.backend.data.model.Article;
import dev.levelupschool.backend.service.interfaces.ArticleService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<CreateArticleResponse> store(
        @RequestBody CreateArticleRequest createArticleRequest,
        HttpServletRequest httpServletRequest) {
        String authHeader = httpServletRequest.getHeader("Authorization");
        return new ResponseEntity<>(articleService.createArticle(createArticleRequest, authHeader), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> update(
        @RequestBody UpdateArticleRequest updateArticleRequest,
        @PathVariable Long id,
        HttpServletRequest httpServletRequest) {
        String authHeader = httpServletRequest.getHeader("Authorization");
        return ResponseEntity.ok(articleService.updateArticle(updateArticleRequest, id, authHeader));
    }

    @DeleteMapping("/{id}")
    public void delete(
        @PathVariable Long id,
        HttpServletRequest httpServletRequest) {
        String authHeader = httpServletRequest.getHeader("Authorization");
        articleService.deleteArticle(id, authHeader);
    }
}
