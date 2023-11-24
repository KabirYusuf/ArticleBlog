package dev.levelupschool.backend.data.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddCommentResponse {
    private Long id;
    private Long articleId;
    private String content;
}
