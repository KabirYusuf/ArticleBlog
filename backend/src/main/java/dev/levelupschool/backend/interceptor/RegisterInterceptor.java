package dev.levelupschool.backend.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Component
public class RegisterInterceptor implements WebMvcConfigurer {
    private final AppUsageInterceptor appUsageInterceptor;
    public RegisterInterceptor(AppUsageInterceptor appUsageInterceptor){
        this.appUsageInterceptor = appUsageInterceptor;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(appUsageInterceptor);
    }
}
