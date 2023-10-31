package dev.levelupschool.backend;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CommentController {
    private final CommentRepository commentRepository;

    private final ArticleRepository articleRepository;

    CommentController(CommentRepository commentRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    @GetMapping("/comments")
    List<Comment> index() {
        return commentRepository.findAll();
    }

    @GetMapping("/comments/{id}")
    Comment show(@PathVariable Long id) {
        return commentRepository
            .findById(id)
            .orElseThrow(() -> new ModelNotFoundException(id));
    }

    @PostMapping("/comments")
    Comment store(@RequestBody Map<String, Object> request) {
        Long articleId = ((Number) request.get("articleId")).longValue();
        String content = (String) request.get("content");

        return commentRepository.save(
            new Comment(
                content,
                articleRepository.findById(articleId).orElseThrow()
            )
        );
    }

    @PutMapping("/comments/{id}")
    Comment update(@RequestBody Comment newComment, @PathVariable Long id) {
        return commentRepository
            .findById(id)
            .map(comment -> {
                comment.setContent(newComment.getContent());
                return commentRepository.save(comment);
            })
            .orElseThrow(() -> new ModelNotFoundException(id));
    }

    @DeleteMapping("/comments/{id}")
    void delete(@PathVariable Long id) {
        commentRepository.deleteById(id);
    }
}
