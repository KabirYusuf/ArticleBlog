package dev.levelupschool.backend.service.interfaces;

import dev.levelupschool.backend.data.dto.request.*;
import dev.levelupschool.backend.data.dto.response.ArticleDTO;
import dev.levelupschool.backend.data.dto.response.AuthenticationResponse;
import dev.levelupschool.backend.data.dto.response.CreateUserResponse;
import dev.levelupschool.backend.data.dto.response.UserDTO;
import dev.levelupschool.backend.data.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

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
    void bookmarkArticle(Long articleId, String authHeader);
    void unBookmarkArticle(Long articleId, String authHeader);
    List<ArticleDTO> getBookmarkedArticles(String authHeader);
    String becomePremium(PaymentDetails paymentDetails, String authHeader);

}
