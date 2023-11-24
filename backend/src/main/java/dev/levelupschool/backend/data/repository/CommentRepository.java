package dev.levelupschool.backend.data.repository;

import dev.levelupschool.backend.data.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
