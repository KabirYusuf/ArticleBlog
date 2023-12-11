package dev.levelupschool.backend.data.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VerifyUserRequest {
    @NotBlank(message = "Enter a valid token")
    @Size(min = 4, max = 4, message = "Verification token must be exactly four digits")
    private String verificationToken;
}
