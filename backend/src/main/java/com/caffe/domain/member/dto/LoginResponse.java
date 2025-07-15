package com.caffe.domain.member.dto;

import com.caffe.domain.member.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private String email;
    private String username;
    private Role role;

    public LoginResponse(String token, String email, String username, Role role) {
        this.token = token;
        this.email = email;
        this.username = username;
        this.role = role;
    }
}