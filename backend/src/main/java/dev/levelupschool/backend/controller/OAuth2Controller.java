package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.security.oauth2.CustomOAuth2User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {
    @GetMapping("/loginSuccess")
    public ResponseEntity<?> getLoginInfo(@AuthenticationPrincipal OAuth2User principal) {
        if (principal instanceof CustomOAuth2User) {
            String token = ((CustomOAuth2User) principal).getToken();

            return ResponseEntity.ok().body(Map.of("token", token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

//    @GetMapping()
//    public ResponseEntity<?> root(@AuthenticationPrincipal OAuth2User principal) {
//        if (principal instanceof CustomOAuth2User) {
//            String token = ((CustomOAuth2User) principal).getToken();
//
//            return ResponseEntity.ok().body(Map.of("token", token));
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    }
}
