CREATE TABLE user_article_reactions (
                                        user_id INT NOT NULL,
                                        article_id INT NOT NULL,
                                        reaction VARCHAR(255) NOT NULL,
                                        PRIMARY KEY (user_id, article_id),
                                        FOREIGN KEY (user_id) REFERENCES users(id),
                                        FOREIGN KEY (article_id) REFERENCES articles(id)
);
CREATE INDEX idx_user_id ON user_article_reactions (user_id);
CREATE INDEX idx_article_id ON user_article_reactions (article_id);

