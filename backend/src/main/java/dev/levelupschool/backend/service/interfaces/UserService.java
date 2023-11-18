package dev.levelupschool.backend.service.interfaces;

import dev.levelupschool.backend.data.dto.request.CreateUserRequest;
import dev.levelupschool.backend.data.dto.request.UpdateUserRequest;
import dev.levelupschool.backend.data.dto.response.CreateUserResponse;
import dev.levelupschool.backend.data.model.User;

import java.util.List;

public interface UserService {
    CreateUserResponse createUser(CreateUserRequest createUserRequest);
    User findUserById(Long authorId);
    List<User> findAllUsers();
    void deleteUser(Long authorId);
    User updateUser(UpdateUserRequest updateUserRequest, Long authorId);
}
