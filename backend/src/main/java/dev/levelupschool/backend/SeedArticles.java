package dev.levelupschool.backend;

import dev.levelupschool.backend.data.model.Article;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.model.Comment;
import dev.levelupschool.backend.data.repository.ArticleRepository;
import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.data.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class SeedArticles {
    private static final Logger log = LoggerFactory.getLogger(SeedArticles.class);

    @Bean
    CommandLineRunner init(
        ArticleRepository articleRepository,
        CommentRepository commentRepository,
        UserRepository userRepository
    ) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                if (articleRepository.count() == 0) {
                    log.info("Seeding articles");

                    User newUser = new User();
                    newUser.setFirstName("Luka");
                    newUser.setLastName("Papez");

                    User savedUser = userRepository.save(newUser);

                    var article1 = articleRepository.save(new Article("test title 1", "test content 1", savedUser));
                    articleRepository.save(new Article("test title 2", "test content 2", savedUser));

                    commentRepository.save(new Comment("test comment", article1, savedUser));
                }
            }
        };
    }
}
