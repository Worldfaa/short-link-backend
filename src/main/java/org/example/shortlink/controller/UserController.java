package org.example.shortlink.controller;

import org.example.shortlink.common.ApiResponse;
import org.example.shortlink.dto.LoginRequest;
import org.example.shortlink.dto.LoginResponse;
import org.example.shortlink.dto.RegisterRequest;
import org.example.shortlink.entity.User;
import org.example.shortlink.repository.UserRepository;
import org.example.shortlink.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    // 注册
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(
            @RequestBody RegisterRequest request)
    {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.ok(
                    ApiResponse.error("用户名已存在")
            );
        }

        User user = new User();
        user.setUsername(request.getUsername());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(request.getPassword()));
        user.getRoles().add("ROLE_USER");

        userRepository.save(user);

        return ResponseEntity.ok(
                ApiResponse.success(user, "注册成功")
        );
    }

    // 登录
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @RequestBody LoginRequest request)
    {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );


        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow();

        String token = JwtUtil.generateToken(user.getUsername(), user.getRoles());

        LoginResponse response =
                new LoginResponse(token, user);

        return ResponseEntity.ok(
                ApiResponse.success(response, "登录成功")
        );
    }
}