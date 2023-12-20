package dev.levelupschool.backend.security.oauth2;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Setter
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {
    private String token;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes,
                            String nameAttributeKey,
                            String token) {
        super(authorities, attributes, nameAttributeKey);
        this.token = token;
    }


}
