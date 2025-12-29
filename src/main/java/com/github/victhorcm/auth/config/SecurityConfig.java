package com.github.victhorcm.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Autowired
    JwtFilter securityFilter;

@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

 
    
    http
    .csrf(csrf -> csrf.disable())
    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    .authorizeHttpRequests( auth -> auth
    .requestMatchers("/auth/signup","/auth/login","/h2-console/**","/auth/refresh-token").permitAll()
    .requestMatchers("/v3/api-docs/**").permitAll()
    .requestMatchers("/swagger-ui/**").permitAll()
    .requestMatchers("/swagger-ui.html").permitAll()
    .anyRequest().authenticated()

    )
    .headers(headers -> headers.frameOptions(frame -> frame.disable()))
    .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}

@Bean
public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}


}
