package org.example.shortlink.filter;

import org.example.shortlink.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            System.out.println("===== JWT Debug Start =====");
            System.out.println("Raw JWT token: " + token);

            try {
                String username = JwtUtil.getUsername(token);

                // 使用 Collection 接收，兼容 List 或 Set
                Collection<String> rolesRaw = JwtUtil.getRoles(token);

                // 转成 List 方便流处理
                List<String> roles = new ArrayList<>(rolesRaw);

                System.out.println("JWT username: " + username);
                System.out.println("JWT roles: " + roles);

                // 映射为 Spring Security 的权限
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                roles.stream()
                                        .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                                        .map(SimpleGrantedAuthority::new)
                                        .toList()
                        );

                SecurityContextHolder.getContext().setAuthentication(authToken);

            } catch (Exception e) {
                System.out.println("JWT token invalid or expired: " + e.getClass() + " - " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"message\":\"Token无效或已过期\"}");
                return;
            }

            System.out.println("===== JWT Debug End =====");
        } else {
            System.out.println("No Authorization header or doesn't start with Bearer");
        }

        filterChain.doFilter(request, response);
    }
}