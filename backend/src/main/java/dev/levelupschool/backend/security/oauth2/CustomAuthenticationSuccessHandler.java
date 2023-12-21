package dev.levelupschool.backend.security.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Value("${OAUTH_SUCCESS_REDIRECT}")
    private String frontendUrl;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        if (authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {

            String email = oidcUser.getAttribute("email");
            String name = oidcUser.getAttribute("name");
            if (name == null) {
                name = oidcUser.getAttribute("given_name");
            }
            assert name != null;
            assert email != null;
            frontendUrl += "?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8)
                + "&name=" + URLEncoder.encode(name, StandardCharsets.UTF_8);
        }
        response.sendRedirect(frontendUrl);
    }
}
