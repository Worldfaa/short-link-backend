package org.example.shortlink.dto;

import org.example.shortlink.entity.User;

public class LoginResponse
{

    private String token;
    private User user;

    public LoginResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() { return token; }
    public User getUser() { return user; }
}