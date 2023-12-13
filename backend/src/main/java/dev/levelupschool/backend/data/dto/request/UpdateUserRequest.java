package dev.levelupschool.backend.data.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class UpdateUserRequest {
    @NotBlank(message = "This field cannot be empty or null")
    private String firstName;
    @NotBlank(message = "This field cannot be empty or null")
    private String lastName;
    private String userImage;
    private String password;
}
