package dev.levelupschool.backend.data.model;

import dev.levelupschool.backend.data.model.enums.ReactionType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.hateoas.server.core.Relation;


import java.util.HashSet;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "articles", schema = "public")
@Relation(collectionRelation = "items")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String articleImage;
    @ManyToMany(mappedBy = "bookmarkedArticles")
    private Set<User> bookmarkedByUsers = new HashSet<>();

    public Set<UserArticleReaction> getReactions() {
        return reactions;
    }

    public void setReactions(Set<UserArticleReaction> reactions) {
        this.reactions = reactions;
    }

    @OneToMany(mappedBy = "article")
    private Set<UserArticleReaction> reactions = new HashSet<>();

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Set<User> getBookmarkedByUsers() {
        return bookmarkedByUsers;
    }

    public void setBookmarkedByUsers(Set<User> bookmarkedByUsers) {
        this.bookmarkedByUsers = bookmarkedByUsers;
    }

    public String getArticleImage() {
        return articleImage;
    }

    public void setArticleImage(String articleImage) {
        this.articleImage = articleImage;
    }


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
        this.updatedAt = updatedAt;
    }

    @CreationTimestamp
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Article(String title, String content, User user, String articleImage) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.articleImage = articleImage;
    }

    public Article() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Comment> getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return String.format("Article[id=%d, title='%s', content='%s']", id, title, content);
    }
}
