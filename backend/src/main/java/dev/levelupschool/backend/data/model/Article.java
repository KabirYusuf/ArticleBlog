package dev.levelupschool.backend.data.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "articles", schema = "public")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Comment> comments;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Article(String title, String content, Author author) {
        this.title = title;
        this.content = content;
        this.author = author;
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
