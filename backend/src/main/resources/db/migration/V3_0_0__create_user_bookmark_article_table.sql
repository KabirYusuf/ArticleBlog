CREATE TABLE user_bookmarked_articles (
                                          user_id INTEGER NOT NULL,
                                          article_id INTEGER NOT NULL,
                                          PRIMARY KEY (user_id, article_id),
                                          FOREIGN KEY (user_id) REFERENCES users(id),
                                          FOREIGN KEY (article_id) REFERENCES articles(id)
);

CREATE INDEX idx_user_bookmarked_articles_user_id ON user_bookmarked_articles(user_id);
CREATE INDEX idx_user_bookmarked_articles_article_id ON user_bookmarked_articles(article_id);
