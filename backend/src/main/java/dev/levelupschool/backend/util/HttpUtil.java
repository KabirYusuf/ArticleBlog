package dev.levelupschool.backend.util;

import jakarta.servlet.http.HttpServletRequest;

public class HttpUtil {
    public static String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
