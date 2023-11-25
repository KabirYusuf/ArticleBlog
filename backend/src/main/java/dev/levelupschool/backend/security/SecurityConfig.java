package dev.levelupschool.backend.security;

import dev.levelupschool.backend.data.model.enums.Role;
import dev.levelupschool.backend.exception.SecurityException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(
        JwtAuthenticationFilter jwtAuthenticationFilter,
        AuthenticationProvider authenticationProvider,
        AccessDeniedHandler accessDeniedHandler,
        AuthenticationEntryPoint authenticationEntryPoint){
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationProvider = authenticationProvider;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        try {
            httpSecurity
                .csrf()
                .disable()
                .exceptionHandling(exceptionHandlingConfigurer -> {
                    exceptionHandlingConfigurer.authenticationEntryPoint(authenticationEntryPoint);
                    exceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler);
                })
                .authorizeHttpRequests((authz) -> authz
<<<<<<< HEAD
                    .requestMatchers("/**").permitAll()
=======
                    .requestMatchers(HttpMethod.GET, "/articles", "/articles/**","/comments").permitAll()
                    .requestMatchers("/auth/register", "/auth/login").permitAll()
                    .requestMatchers("/app-usage/**").hasAuthority(Role.ADMIN.name())
>>>>>>> 19eb3cd (feat: implemented article view and routing)
                    .anyRequest().authenticated()
                )
                .sessionManagement(session-> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        } catch (Exception e) {
            throw new SecurityException(e.getMessage());
        }

        try {
            return httpSecurity.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
