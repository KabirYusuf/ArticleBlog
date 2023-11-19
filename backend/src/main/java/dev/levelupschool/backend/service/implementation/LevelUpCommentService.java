package dev.levelupschool.backend.service.implementation;

import dev.levelupschool.backend.data.dto.request.AddCommentRequest;
import dev.levelupschool.backend.data.dto.request.UpdateCommentRequest;
import dev.levelupschool.backend.data.dto.response.AddCommentResponse;
import dev.levelupschool.backend.data.model.Article;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.model.Comment;
import dev.levelupschool.backend.data.repository.CommentRepository;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.exception.UserException;
import dev.levelupschool.backend.service.interfaces.ArticleService;
import dev.levelupschool.backend.service.interfaces.UserService;
import dev.levelupschool.backend.service.interfaces.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class LevelUpCommentService implements CommentService {
    private final CommentRepository commentRepository;
    private final ArticleService articleService;
    private final UserService userService;
    public LevelUpCommentService(
        CommentRepository commentRepository,
        ArticleService articleService,
        UserService userService){
        this.commentRepository = commentRepository;
        this.articleService = articleService;
        this.userService = userService;
    }

    @Override
    public AddCommentResponse addComment(AddCommentRequest addCommentRequest, String authHeader) {
        User fondUser = userService.getUser(authHeader);
        Article foundArticle = articleService.findArticleById(addCommentRequest.getArticleId());
        Comment newComment = new Comment(addCommentRequest.getContent(), foundArticle, fondUser);
        Comment savedComment = commentRepository.save(newComment);
        AddCommentResponse addCommentResponse = new AddCommentResponse();
        addCommentResponse.setId(savedComment.getId());
        addCommentResponse.setContent(savedComment.getContent());
        addCommentResponse.setArticleId(savedComment.getArticle().getId());
        return addCommentResponse;
    }

    @Override
    public Page<Comment> findAllComments(Pageable pageable) {
        return commentRepository
            .findAll(pageable);
    }

    @Override
    public Comment findCommentById(Long commentId) {
        return commentRepository
            .findById(commentId)
            .orElseThrow(()-> new ModelNotFoundException(commentId));
    }

    @Override
    public Comment updateComment(UpdateCommentRequest updateCommentRequest, Long commentId, String authHeader) {
        User foundUser = userService.getUser(authHeader);
        return commentRepository
            .findById(commentId)
            .map(comment -> {
                if (!Objects.equals(foundUser.getId(), comment.getUser().getId()))throw new UserException("You have no permission to update comment");
                comment.setContent(updateCommentRequest.getContent());
                return commentRepository.save(comment);
            })
            .orElseThrow(()-> new ModelNotFoundException(commentId));
    }

    @Override
    public void deleteComment(Long commentId, String authHeader) {
        User foundUser = userService.getUser(authHeader);
        Comment foundComment = commentRepository.findById(commentId)
                .orElseThrow(()-> new ModelNotFoundException(commentId));
        if (!Objects.equals(foundUser.getId(), foundComment.getUser().getId()))throw new UserException("You have no permission to update comment");
        commentRepository
            .deleteById(commentId);
    }
}
