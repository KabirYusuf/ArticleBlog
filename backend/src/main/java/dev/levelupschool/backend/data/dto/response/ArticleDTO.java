package dev.levelupschool.backend.data.dto.response;

import dev.levelupschool.backend.data.model.Article;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ArticleDTO {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String articleImage;

    public ArticleDTO(Article article){
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.articleImage = article.getArticleImage();
        this.author = article.getUser().getUsername();
    }
}
