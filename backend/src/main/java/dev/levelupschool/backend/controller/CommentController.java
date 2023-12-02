package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.data.dto.request.AddCommentRequest;
import dev.levelupschool.backend.data.dto.request.UpdateCommentRequest;
import dev.levelupschool.backend.data.dto.response.AddCommentResponse;
import dev.levelupschool.backend.data.model.Article;
import dev.levelupschool.backend.data.repository.ArticleRepository;
import dev.levelupschool.backend.data.repository.CommentRepository;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.data.model.Comment;
import dev.levelupschool.backend.service.interfaces.ArticleService;
import dev.levelupschool.backend.service.interfaces.CommentService;
import dev.levelupschool.backend.util.CommentResourceAssembler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final CommentResourceAssembler commentResourceAssembler;

    public CommentController(
        CommentService commentService,
        CommentResourceAssembler commentResourceAssembler) {
        this.commentService = commentService;
        this.commentResourceAssembler = commentResourceAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Comment>>> index(Pageable pageable) {
        Page<Comment> comments = commentService.findAllComments(pageable);
        PagedModel<EntityModel<Comment>> pagedModel = commentResourceAssembler.toPagedModel(comments, pageable);

        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> show(@PathVariable Long id) {
       return ResponseEntity.ok(commentService.findCommentById(id));
    }

    @PostMapping
    public ResponseEntity<AddCommentResponse> store(
        @RequestBody @Valid AddCommentRequest addCommentRequest,
        HttpServletRequest httpServletRequest) {
        String authHeader = httpServletRequest.getHeader("Authorization");
        return new ResponseEntity<>(commentService.addComment(addCommentRequest, authHeader), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> update(
        @RequestBody UpdateCommentRequest updateCommentRequest,
        @PathVariable Long id,
        HttpServletRequest httpServletRequest) {
        String authHeader = httpServletRequest.getHeader("Authorization");
        return ResponseEntity.ok(commentService.updateComment(updateCommentRequest, id, authHeader));
    }

    @DeleteMapping("/{id}")
    public void delete(
        @PathVariable Long id,
        HttpServletRequest httpServletRequest) {
        String authHeader = httpServletRequest.getHeader("Authorization");
        commentService.deleteComment(id, authHeader);
    }
}
