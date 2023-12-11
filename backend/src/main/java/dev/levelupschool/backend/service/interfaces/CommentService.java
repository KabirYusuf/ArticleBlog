package dev.levelupschool.backend.service.interfaces;

import dev.levelupschool.backend.data.dto.request.AddCommentRequest;
import dev.levelupschool.backend.data.dto.request.UpdateCommentRequest;
import dev.levelupschool.backend.data.dto.response.AddCommentResponse;
import dev.levelupschool.backend.data.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    AddCommentResponse addComment(AddCommentRequest addCommentRequest, String authHeader);
    Page<Comment> findAllComments(Pageable pageable);
    Comment findCommentById(Long commentId);
    Comment updateComment(UpdateCommentRequest updateCommentRequest, Long commentId, String authHeader);
    void deleteComment(Long commentId, String authHeader);
}
