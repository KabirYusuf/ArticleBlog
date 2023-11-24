package dev.levelupschool.backend.data.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateArticleRequest {
    private String title;
    private String content;
    private Long authorId;
}
