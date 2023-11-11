package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.data.dto.request.AddCommentRequest;
import dev.levelupschool.backend.data.dto.request.UpdateCommentRequest;
import dev.levelupschool.backend.data.dto.response.AddCommentResponse;
import dev.levelupschool.backend.data.repository.ArticleRepository;
import dev.levelupschool.backend.data.repository.CommentRepository;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.data.model.Comment;
import dev.levelupschool.backend.service.interfaces.ArticleService;
import dev.levelupschool.backend.service.interfaces.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<Comment>> index() {
        return ResponseEntity.ok(commentService.findAllComments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> show(@PathVariable Long id) {
       return ResponseEntity.ok(commentService.findCommentById(id));
    }

    @PostMapping
    public ResponseEntity<AddCommentResponse> store(@RequestBody AddCommentRequest addCommentRequest) {
        return new ResponseEntity<>(commentService.addComment(addCommentRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> update(@RequestBody UpdateCommentRequest updateCommentRequest, @PathVariable Long id) {
        return ResponseEntity.ok(commentService.updateComment(updateCommentRequest, id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        commentService.deleteComment(id);
    }
}
