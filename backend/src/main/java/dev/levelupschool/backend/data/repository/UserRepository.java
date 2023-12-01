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
<<<<<<< HEAD
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.bookmarkedArticles WHERE u.id = :userId")
    Optional<User> findByIdWithBookmarkedArticles(Long userId);
=======
>>>>>>> 154319b (feat: Implemented many-many r/shp for users and followers with all unit and integration test passing)
=======
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.bookmarkedArticles WHERE u.id = :userId")
    Optional<User> findByIdWithBookmarkedArticles(Long userId);
>>>>>>> 86c245a (feat: implemented many-many r/shp between user and articles for bookmarked article, all unit and integration test passing and database base migration script written to create a table for the r/shp)
}
