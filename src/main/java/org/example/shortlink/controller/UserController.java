package org.example.shortlink.controller;

import org.example.shortlink.dto.LoginRequest;
import org.example.shortlink.dto.RegisterRequest;
import org.example.shortlink.entity.User;
import org.example.shortlink.repository.UserRepository;
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
public class UserController
{
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request)
    {
        if (userRepository.findByUsername(request.getUsername()).isPresent())
        {
            return ResponseEntity.badRequest().body("用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());

        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(request.getPassword()));

        user.getRoles().add("ROLE_USER");

        userRepository.save(user);

        return ResponseEntity.ok("注册成功");
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request)
    {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(authToken);
        if (authentication.isAuthenticated())
        {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok("登录成功");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("登录失败");
    }
}
