package ru.fishexam.fishexam.auth.models;

import ru.fishexam.fishexam.dto.user.UserRole;

public class UserAuth {
    private Long userId;
    private String username;
    private String password;
    private UserRole userRole;

    public UserAuth(Long userId, String username, String password, UserRole userRole) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.userRole = userRole;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
