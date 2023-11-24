package dev.levelupschool.backend.service.implementation;

import dev.levelupschool.backend.data.dto.request.AddCommentRequest;
import dev.levelupschool.backend.data.dto.request.UpdateCommentRequest;
import dev.levelupschool.backend.data.dto.response.AddCommentResponse;
import dev.levelupschool.backend.data.model.Article;
import dev.levelupschool.backend.data.model.Author;
import dev.levelupschool.backend.data.model.Comment;
import dev.levelupschool.backend.data.repository.CommentRepository;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.service.interfaces.ArticleService;
import dev.levelupschool.backend.service.interfaces.AuthorService;
import dev.levelupschool.backend.service.interfaces.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelUpCommentService implements CommentService {
    private final CommentRepository commentRepository;
    private final ArticleService articleService;
    private final AuthorService authorService;
    public LevelUpCommentService(
        CommentRepository commentRepository,
        ArticleService articleService,
        AuthorService authorService){
        this.commentRepository = commentRepository;
        this.articleService = articleService;
        this.authorService = authorService;
    }

    @Override
    public AddCommentResponse addComment(AddCommentRequest addCommentRequest) {
        Author fondAuthor = authorService.findAuthorById(addCommentRequest.getAuthorId());
        Article foundArticle = articleService.findArticleById(addCommentRequest.getArticleId());
        Comment newComment = new Comment(addCommentRequest.getContent(), foundArticle, fondAuthor);
        Comment savedComment = commentRepository.save(newComment);
        AddCommentResponse addCommentResponse = new AddCommentResponse();
        addCommentResponse.setId(savedComment.getId());
        addCommentResponse.setContent(savedComment.getContent());
        addCommentResponse.setArticleId(savedComment.getArticle().getId());
        return addCommentResponse;
    }

    @Override
    public List<Comment> findAllComments() {
        return commentRepository
            .findAll();
    }

    @Override
    public Comment findCommentById(Long commentId) {
        return commentRepository
            .findById(commentId)
            .orElseThrow(()-> new ModelNotFoundException(commentId));
    }

    @Override
    public Comment updateComment(UpdateCommentRequest updateCommentRequest, Long commentId) {
        return commentRepository
            .findById(commentId)
            .map(comment -> {
                comment.setContent(updateCommentRequest.getContent());
                return commentRepository.save(comment);
            })
            .orElseThrow(()-> new ModelNotFoundException(commentId));
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository
            .deleteById(commentId);
    }
}
