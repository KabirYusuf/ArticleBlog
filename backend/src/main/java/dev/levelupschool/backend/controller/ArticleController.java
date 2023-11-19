package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.data.dto.request.CreateArticleRequest;
import dev.levelupschool.backend.data.dto.request.UpdateArticleRequest;
import dev.levelupschool.backend.data.dto.response.CreateArticleResponse;
import dev.levelupschool.backend.data.model.Article;
import dev.levelupschool.backend.service.interfaces.ArticleService;
import dev.levelupschool.backend.util.ArticleResourceAssembler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;
    private final ArticleResourceAssembler articleResourceAssembler;

    public ArticleController(
        ArticleService articleService,
        ArticleResourceAssembler articleResourceAssembler) {
        this.articleService = articleService;
        this.articleResourceAssembler = articleResourceAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Article>>> index(Pageable pageable) {
        Page<Article> articles = articleService.findAllArticle(pageable);
        PagedModel<EntityModel<Article>> pagedModel = articleResourceAssembler.toPagedModel(articles, pageable);

        return ResponseEntity.ok(pagedModel);
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
