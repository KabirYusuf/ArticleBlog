package dev.levelupschool.backend.data.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dev.levelupschool.backend.data.model.Article;
import jakarta.persistence.*;

@Entity
@Table(name = "comments", schema = "public")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    @JsonBackReference
    private Article article;

    private String content;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    public Comment(String content, Article article, Author author) {
        this.content = content;
        this.article = article;
        this.author = author;
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

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
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
