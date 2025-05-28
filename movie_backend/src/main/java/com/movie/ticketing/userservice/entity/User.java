package com.movie.ticketing.userservice.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    // Getters and Setters
    private Long id;
    @NotBlank(message = "用户名不能为空")
    private String username;
    private String nickname;
    @Email(message = "邮箱格式不正确")
    @NotBlank(message = "邮箱不能为空")
    private String email;
    @NotBlank(message = "密码不能为空")
    private String password;
    private String role;

    // Constructors
    public User() {}

    public User(Long id, String username, String nickname, String email, String password, String role) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
