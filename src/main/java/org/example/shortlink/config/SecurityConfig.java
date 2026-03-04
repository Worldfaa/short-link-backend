package org.example.shortlink.config;

import org.example.shortlink.filter.JwtAuthenticationFilter;
import org.example.shortlink.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // 禁用 CSRF，便于测试

                // 权限配置
                .authorizeHttpRequests(auth -> auth
                        // 注册登录接口公开
                        .requestMatchers("/api/user/register", "/api/user/login").permitAll()

                        // 管理端接口，需要 ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 短链操纵接口，需要 ADMIN
                        .requestMatchers("/api/short-url/disable/**").hasRole("ADMIN")
                        .requestMatchers("/api/short-url/enable/**").hasRole("ADMIN")
                        .requestMatchers("/api/short-url/delete/**").hasRole("ADMIN")

                        // 其他接口需要登录
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form.disable())   // 禁用默认表单登录
                .logout(logout -> logout.permitAll()) // 登出接口公开

                // JWT 过滤器加入
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 密码加密器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 让 Controller 可以注入 AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService();
    }
}