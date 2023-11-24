package dev.levelupschool.backend.data.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateArticleRequest {
    private String title;
    private String content;
}
