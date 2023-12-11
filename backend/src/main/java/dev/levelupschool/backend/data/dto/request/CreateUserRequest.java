package dev.levelupschool.backend.data.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
}
