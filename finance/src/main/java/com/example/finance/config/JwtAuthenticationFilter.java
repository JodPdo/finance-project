package com.example.finance.config;

import com.example.finance.config.JwtUtil;
import com.example.finance.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        System.out.println("✅ JwtAuthenticationFilter ถูกเรียกแล้ว");

        final String authHeader = request.getHeader("Authorization");
        String email = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // ตัดคำว่า Bearer ออก
            email = jwtUtil.extractEmail(jwt); // ดึง email จาก token
            System.out.println("📧 Email จาก Token: " + email);
        } else {
            System.out.println("❌ ไม่มี Authorization Header หรือไม่ได้ขึ้นต้นด้วย Bearer");
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = userDetailsService.loadUserByUsername(email);
            System.out.println("👤 โหลด UserDetails: " + userDetails.getUsername());

            if (jwtUtil.isTokenValid(jwt, userDetails.getUsername())) {
                System.out.println("🟢 Token ผ่านการตรวจสอบ");

                var authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("✅ ตั้งค่า SecurityContext เรียบร้อยแล้ว");
            } else {
                System.out.println("🔴 Token ไม่ผ่านการตรวจสอบ (Invalid)");
            }
        }

        filterChain.doFilter(request, response);
    }
}


