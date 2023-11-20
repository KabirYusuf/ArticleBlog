package dev.levelupschool.backend.data.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthenticationResponse {
    private String token;
    private String message;
}
