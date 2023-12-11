package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.interceptor.AppUsageInterceptor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/app-usage")
public class InterceptorController {
    private final AppUsageInterceptor appUsageInterceptor;
    public InterceptorController(AppUsageInterceptor appUsageInterceptor){
        this.appUsageInterceptor = appUsageInterceptor;
    }

    @GetMapping
    public ResponseEntity<ConcurrentHashMap<String, Integer>> getAppUsageStatistics(){
        return ResponseEntity.ok(appUsageInterceptor.getUsageMap());
    }
}
