package dev.levelupschool.backend.util;

import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.model.enums.Role;
import dev.levelupschool.backend.data.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
public class UserUtil {
    private final UserRepository userRepository;

    public UserUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User processOAuth2User(String email, String name) {
        User user = userRepository.findUsersByUsernameIgnoreCase(email).orElse(null);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setUsername(email);
            user.setFirstName(name);
            user.setPassword(UUID.randomUUID().toString());
            user.setVerified(true);
            user.setRoles(Set.of(Role.USER));
            userRepository.save(user);
        }
        return user;
    }
}
