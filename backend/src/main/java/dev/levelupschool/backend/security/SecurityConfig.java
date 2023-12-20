package dev.levelupschool.backend.security;

import dev.levelupschool.backend.data.model.enums.Role;
import dev.levelupschool.backend.exception.SecurityException;
import dev.levelupschool.backend.security.oauth2.CustomAuthenticationErrorHandler;
import dev.levelupschool.backend.security.oauth2.CustomAuthenticationSuccessHandler;
import dev.levelupschool.backend.security.oauth2.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    private final CustomAuthenticationErrorHandler authenticationErrorHandler;

    public SecurityConfig(
        JwtAuthenticationFilter jwtAuthenticationFilter,
        AuthenticationProvider authenticationProvider,
        AccessDeniedHandler accessDeniedHandler,
        AuthenticationEntryPoint authenticationEntryPoint,
        CustomOAuth2UserService customOAuth2UserService, CustomAuthenticationSuccessHandler authenticationSuccessHandler, CustomAuthenticationErrorHandler authenticationErrorHandler){
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationProvider = authenticationProvider;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.customOAuth2UserService = customOAuth2UserService;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationErrorHandler = authenticationErrorHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        try {
            httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandlingConfigurer -> {
                    exceptionHandlingConfigurer.authenticationEntryPoint(authenticationEntryPoint);
                    exceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler);
                })
                .authorizeHttpRequests((authz) -> authz
                    .requestMatchers(HttpMethod.GET, "/articles/**", "/users/**", "/comments/**", "/oauth2/**", "/").permitAll()
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers("/app-usage/**").hasAuthority(Role.ADMIN.name())
                    .anyRequest().authenticated()
                )
                .sessionManagement(session-> session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(Customizer.withDefaults())
                .oauth2Login(oauth2 -> oauth2
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationErrorHandler)
                    .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService)
                    )
                );
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
