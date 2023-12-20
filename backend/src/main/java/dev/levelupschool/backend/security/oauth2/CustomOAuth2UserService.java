package dev.levelupschool.backend.security.oauth2;

import com.nimbusds.jose.crypto.impl.PRFParams;
import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.model.enums.Role;
import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.security.JwtService;
import dev.levelupschool.backend.security.SecuredUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);


        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        log.info("Oauth2:: " + attributes);

        User user = processOAuth2User(email, name);


        String token = jwtService.generateToken(new SecuredUser(user));


        return new CustomOAuth2User(oAuth2User.getAuthorities(), attributes, "email", token);
    }

    private User processOAuth2User(String email, String name) {
        User user = userRepository.findUsersByUsernameIgnoreCase(email).orElse(null);
        if (user == null) {

            user = new User();
            user.setEmail(email);
            user.setUsername(email);
            user.setFirstName(name);
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString().substring(0,7)));
            user.setVerified(true);
            user.setRoles(Set.of(Role.USER));
            userRepository.save(user);
        }
        return user;
    }
}
