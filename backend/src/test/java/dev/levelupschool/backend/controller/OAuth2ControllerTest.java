package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.security.oauth2.CustomOAuth2User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class OAuth2ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private void setSecurityContextWithOAuth2User() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", "John Doe");
        attributes.put("email", "johndoe@example.com");

        OAuth2User oAuth2User = new CustomOAuth2User(
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
            attributes,
            "name",
            "generated-jwt-token"
        );

        Authentication auth = new UsernamePasswordAuthenticationToken(oAuth2User, null, oAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void whenUserIsAuthenticated_thenTokenIsReturned() throws Exception {
        setSecurityContextWithOAuth2User();

        mockMvc.perform(get("/oauth2/loginSuccess"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.token").value("generated-jwt-token"));
    }

    @Test
    void whenUserIsNotAuthenticated_thenUnauthorized() throws Exception {
        SecurityContextHolder.clearContext();

        mockMvc.perform(get("/oauth2/loginSuccess"))
            .andExpect(status().isUnauthorized());
    }
}
