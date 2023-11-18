package dev.levelupschool.backend.security;

import dev.levelupschool.backend.exception.SecurityException;
import dev.levelupschool.backend.util.Serializer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class LevelUpAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException){

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        Map<String, Object> data = new HashMap<>();
        data.put("Status", false);
        data.put("Message", authException.getMessage());

        try {
            response.getOutputStream().print(Serializer.asJsonString(data));
        } catch (IOException e) {
            throw new SecurityException(e.getMessage());
        }
    }
}
