package dev.levelupschool.backend.interceptor;

import dev.levelupschool.backend.exception.InterceptorException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class AppUsageInterceptor implements HandlerInterceptor {
    private ConcurrentHashMap<String, Integer> usageMap = new ConcurrentHashMap<>();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        try {
            log.info("Request time::: " + LocalDateTime.now());
            String path = request.getRequestURI();
            usageMap.merge(path, 1, Integer::sum);
            return true;
        }
        catch (Exception exception){
            throw new InterceptorException(exception.getMessage());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("Response time::: " + LocalDateTime.now());
    }

    public ConcurrentHashMap<String, Integer> getUsageMap(){
        return  usageMap;
    }
}
