package dev.levelupschool.backend.security;

import dev.levelupschool.backend.exception.SecurityException;
import dev.levelupschool.backend.util.Serializer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Component
public class LevelUpAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException){
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        Map<String, Object> data = new HashMap<>();
        data.put("Status", false);
        data.put("Message", accessDeniedException.getMessage());
        try {
            response.getOutputStream().print(Serializer.asJsonString(data));
        } catch (IOException e) {
            throw new SecurityException(e.getMessage());
        }
    }
}
