package dev.levelupschool.backend.security;

import dev.levelupschool.backend.util.Serializer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class LevelUpAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException) {

        log.info("Access denied for request: {}", request.getRequestURI());

        try {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");

            Map<String, Object> data = new HashMap<>();
            data.put("Status", false);
            data.put("Message", "Access Denied: " + accessDeniedException.getMessage());
            response.getOutputStream().print(Serializer.asJsonString(data));

        } catch (IOException e) {
            log.error("Error writing access denied response for request: {}", request.getRequestURI(), e);

            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            try {
                response.getOutputStream().print("{\"Status\": false, \"Message\": \"Internal server error\"}");
            } catch (IOException ioException) {
                log.error("Failed to send internal server error response for request: {}", request.getRequestURI(), ioException);
            }
        }
    }
}
