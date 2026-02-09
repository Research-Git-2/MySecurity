package org.example.mysecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/secret").hasRole("ADMIN")
                        .requestMatchers("/home/").authenticated()
                        .requestMatchers("/auth/login", "/auth/register", "auth/forget-password").permitAll()
                        .anyRequest().hasAnyRole("USER", "ADMIN")
                );
        return http.build();
    }
}
