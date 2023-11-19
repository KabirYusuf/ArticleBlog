package dev.levelupschool.backend.service.interfaces;

import dev.levelupschool.backend.data.dto.request.AddCommentRequest;
import dev.levelupschool.backend.data.dto.request.UpdateCommentRequest;
import dev.levelupschool.backend.data.dto.response.AddCommentResponse;
import dev.levelupschool.backend.data.model.Comment;

import java.util.List;

public interface CommentService {
    AddCommentResponse addComment(AddCommentRequest addCommentRequest, String authHeader);
    List<Comment> findAllComments();
    Comment findCommentById(Long commentId);
    Comment updateComment(UpdateCommentRequest updateCommentRequest, Long commentId, String authHeader);
    void deleteComment(Long commentId, String authHeader);
}
