package dev.levelupschool.backend.data.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateArticleRequest {
    @NotBlank(message = "This field cannot be empty or null")
    private String title;
    @NotBlank(message = "This field cannot be empty or null")
    private String content;
}
