package dev.levelupschool.backend.data.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
<<<<<<< HEAD
import java.time.LocalDateTime;
=======
>>>>>>> 561af2f (fix: "resolving conflict between homework3 and storage branch")

@Entity
@Table(name = "comments", schema = "public")
@Relation(collectionRelation = "items")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    @JsonBackReference
    private Article article;

    private String content;

<<<<<<< HEAD
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
=======
    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
>>>>>>> 561af2f (fix: "resolving conflict between homework3 and storage branch")
        this.updatedAt = updatedAt;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
<<<<<<< HEAD
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
=======
    private LocalDate createdAt;
    @LastModifiedDate
    private LocalDate updatedAt;
>>>>>>> 561af2f (fix: "resolving conflict between homework3 and storage branch")

    public Comment(String content, Article article, User user) {
        this.content = content;
        this.article = article;
        this.user = user;
    }

    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    @Override
    public String toString() {
        return String.format("Comment[id=%d, article.id=%d, content='%s']", id, article.getId(), content);
    }
}
