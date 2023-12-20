package dev.levelupschool.backend.security.oauth2;

import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.model.enums.Role;
import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.security.JwtService;
import dev.levelupschool.backend.security.SecuredUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String email = extractEmail(attributes, registrationId);
        String name = extractName(attributes, registrationId);

        User user = processOAuth2User(email, name);
        String token = jwtService.generateToken(new SecuredUser(user));
        return new CustomOAuth2User(oAuth2User.getAuthorities(), attributes, "email", token);
    }

    private String extractEmail(Map<String, Object> attributes, String registrationId) {
        if ("google".equals(registrationId)) {
            return (String) attributes.get("email");
        } else if ("gitlab".equals(registrationId)) {
            return (String) attributes.get("email");
        }
        return null;
    }

    private String extractName(Map<String, Object> attributes, String registrationId) {
        if ("google".equals(registrationId)) {
            return (String) attributes.get("name");
        } else if ("gitlab".equals(registrationId)) {
            // Adjust according to GitLab's attribute structure
            return (String) attributes.get("name");
        }
        return null;
    }

    private User processOAuth2User(String email, String name) {
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
