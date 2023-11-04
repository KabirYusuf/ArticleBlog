package dev.levelupschool.backend.data.repository;

import dev.levelupschool.backend.data.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
