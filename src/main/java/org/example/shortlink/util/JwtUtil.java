package org.example.shortlink.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JwtUtil {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 24小时

    // 生成 token
    public static String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles) // 存 List，不存 Set
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }

    // 解析 token
    public static Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    // 获取用户名
    public static String getUsername(String token) {
        return parseToken(token).getBody().getSubject();
    }

    // 获取角色列表，返回 List<String>
    public static List<String> getRoles(String token) {
        Claims claims = parseToken(token).getBody();
        Object rolesObj = claims.get("roles");
        if (rolesObj instanceof List<?>) {
            return ((List<?>) rolesObj).stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}