package dev.levelupschool.backend;

import dev.levelupschool.backend.data.model.Article;
import dev.levelupschool.backend.data.model.enums.Role;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

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

                    Set<Role> roleSet = new HashSet<>();

                    roleSet.add(Role.USER);
                    roleSet.add(Role.ADMIN);
                    User newUser = new User();
                    newUser.setFirstName("Luka");
                    newUser.setLastName("Papez");
                    newUser.setEmail("luka@gmail.com");
                    newUser.setUsername("lpapez");
                    newUser.setPassword(new BCryptPasswordEncoder().encode("Abc123@lp"));
                    newUser.setRoles(roleSet);
                    newUser.setVerified(true);

                    User savedUser = userRepository.save(newUser);

                    var article1 = articleRepository.save(new Article("test title 1", "test content 1", savedUser, null));
                    articleRepository.save(new Article("test title 2", "test content 2", savedUser, null));

                    commentRepository.save(new Comment("test comment", article1, savedUser));
                }
            }
        };
    }
}
