package dev.levelupschool.backend.data.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateCommentRequest {
    @NotBlank(message = "This field cannot be empty or null")
    private String content;
}
