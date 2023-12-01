package dev.levelupschool.backend.data.repository;

import dev.levelupschool.backend.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUsersByUsernameIgnoreCase(String email);
    Optional<User> findByUsernameIgnoreCase(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.following WHERE u.id = :userId")
    Optional<User> findByIdWithFollowing(Long userId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.followers WHERE u.id = :userId")
    Optional<User> findByIdWithFollowers(Long userId);
<<<<<<< HEAD
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.bookmarkedArticles WHERE u.id = :userId")
    Optional<User> findByIdWithBookmarkedArticles(Long userId);
=======
>>>>>>> 154319b (feat: Implemented many-many r/shp for users and followers with all unit and integration test passing)
}
