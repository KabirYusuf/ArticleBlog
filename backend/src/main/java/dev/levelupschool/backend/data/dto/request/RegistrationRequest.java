package dev.levelupschool.backend.data.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegistrationRequest {
    @Email(message = "enter a valid email")
    private String email;
    @NotBlank(message = "This field cannot be blank")
    private String username;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "Password must be at least 8 characters long " +
            "and include at least one uppercase letter, one lowercase " +
            "letter, one digit, and one special character.")
    private String password;
}
