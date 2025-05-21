package com.example.finance.config;

import com.example.finance.config.JwtAuthenticationFilter;
import com.example.finance.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .cors()
        .and()
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
    .requestMatchers("/api/auth/**").permitAll()

    // ✅ ให้สิทธิ์ทั้ง summary ทั้งหมดแบบครอบคลุม
    .requestMatchers("/api/transactions/summary/**").authenticated()

    // ✅ หรือจะระบุให้ละเอียดก็ได้
    // .requestMatchers("/api/transactions/summary", "/api/transactions/summary/total", "/api/transactions/summary-by-category").authenticated()

    .requestMatchers("/api/transactions/**").authenticated()
    .anyRequest().authenticated()
)


        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
}

}


