package com.project.taskipro.config;

import com.project.taskipro.filter.JwtFilter;
import com.project.taskipro.handler.CustomAccessDeniedHandler;
import com.project.taskipro.handler.CustomLogoutHandler;
import com.project.taskipro.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFIlter;

    private final UserService userService;

    private final CustomAccessDeniedHandler accessDeniedHandler;

    private final CustomLogoutHandler customLogoutHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/auth/login/**", "/auth/registration/**", "/css/**", "/refresh_token/**", "/").permitAll();
            auth.anyRequest().authenticated();
        }).userDetailsService(userService).exceptionHandling(e -> {
            e.accessDeniedHandler(accessDeniedHandler);
            e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        }).sessionManagement(session -> session.sessionCreationPolicy(STATELESS)).addFilterBefore(jwtFIlter, UsernamePasswordAuthenticationFilter.class).logout(log -> {
            log.logoutUrl("/logout");
            log.addLogoutHandler(customLogoutHandler);
            log.logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());
        });

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }

}