package dev.levelupschool.backend;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArticleController {
    private final ArticleRepository articleRepository;

    ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/articles")
    List<Article> index() {
        return articleRepository.findAll();
    }

    @GetMapping("/articles/{id}")
    Article show(@PathVariable Long id) {
        return articleRepository
            .findById(id)
            .orElseThrow(() -> new ModelNotFoundException(id));
    }

    @PostMapping("/articles")
    Article store(@RequestBody Article article) {
        return articleRepository.save(article);
    }

    @PutMapping("/articles/{id}")
    Article update(@RequestBody Article newArticle, @PathVariable Long id) {
        return articleRepository
            .findById(id)
            .map(article -> {
                article.setContent(newArticle.getContent());
                article.setTitle(newArticle.getTitle());
                return articleRepository.save(article);
            })
            .orElseThrow(() -> new ModelNotFoundException(id));
    }

    @DeleteMapping("/articles/{id}")
    void delete(@PathVariable Long id) {
        articleRepository.deleteById(id);
    }
}
