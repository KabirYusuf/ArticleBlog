package dev.levelupschool.backend.data.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthenticationRequest {
    @NotBlank(message = "Enter a valid username")
    private String username;
    @NotBlank(message = "Enter a valid password")
    private String password;
}
