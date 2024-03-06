package dev.levelupschool.backend.security.oauth2;

import dev.levelupschool.backend.data.model.User;
import dev.levelupschool.backend.data.repository.UserRepository;
import dev.levelupschool.backend.security.JwtService;
import dev.levelupschool.backend.security.SecuredUser;
import dev.levelupschool.backend.util.UserUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${OAUTH_SUCCESS_REDIRECT}")
    private String redirectUrl;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserUtil userUtil;

    public CustomAuthenticationSuccessHandler(UserRepository userRepository, JwtService jwtService, UserUtil userUtil) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userUtil = userUtil;
    }

//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                        Authentication authentication) throws IOException, ServletException {
//
//        String token = "";
//
//        if (authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {
//
//            String email = oidcUser.getAttribute("email");
//            String name = "";
//            Map<String, Object> attributes = oidcUser.getAttributes();
//            if (oidcUser.getAttribute("given_name") != null) {
//                name = oidcUser.getAttribute("given_name");
//                User user = userUtil.processOAuth2User(email, name);
//                token = jwtService.generateToken(new SecuredUser(user));
//                new CustomOAuth2User(oidcUser.getAuthorities(), attributes, "email", token);
//                redirectUrl += token;
//            }
//        }
//        else{
//            CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
//            token = oauthUser.getToken();
//            redirectUrl += token;
//        }
//        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
//    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String token = "";
        if (authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {

            String email = oidcUser.getAttribute("email");
            String name = "";
            Map<String, Object> attributes = oidcUser.getAttributes();
            if (oidcUser.getAttribute("given_name") != null) {
                name = oidcUser.getAttribute("given_name");
                User user = userUtil.processOAuth2User(email, name);
                token = jwtService.generateToken(new SecuredUser(user));
            }
        } else {
            CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
            token = oauthUser.getToken();
        }

        String dynamicRedirectUrl = redirectUrl + token;
        getRedirectStrategy().sendRedirect(request, response, dynamicRedirectUrl);
    }

}
