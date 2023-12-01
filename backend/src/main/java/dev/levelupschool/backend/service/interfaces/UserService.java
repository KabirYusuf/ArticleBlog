package dev.levelupschool.backend.service.interfaces;

import dev.levelupschool.backend.data.dto.request.AuthenticationRequest;
import dev.levelupschool.backend.data.dto.request.CreateUserRequest;
import dev.levelupschool.backend.data.dto.request.RegistrationRequest;
import dev.levelupschool.backend.data.dto.request.UpdateUserRequest;
import dev.levelupschool.backend.data.dto.response.AuthenticationResponse;
import dev.levelupschool.backend.data.dto.response.CreateUserResponse;
import dev.levelupschool.backend.data.dto.response.UserDTO;
import dev.levelupschool.backend.data.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    User registerUser(RegistrationRequest registrationRequest);
    User findUserById(Long authorId);
    Page<User> findAllUsers(Pageable pageable);
    void deleteUser(Long userId, String authHeader);
    User updateUser(UpdateUserRequest updateUserRequest, Long userId, String authHeader);

    User findUserByUsername(String username);

    User getUser(String authHeader);
    void followUser(String authHeader, Long followedUserId);
    void unfollowUser(String authHeader, Long followedUserId);
    List<UserDTO> getFollowers(String authHeader);
    List<UserDTO> getUsersFollowed(String authHeader);

}
