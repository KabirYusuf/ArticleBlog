package dev.levelupschool.backend.data.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateArticleResponse {
    private Long id;
    private String title;
    private String content;
}
