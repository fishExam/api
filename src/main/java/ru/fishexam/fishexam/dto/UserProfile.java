package ru.fishexam.fishexam.dto;

import jakarta.annotation.Nullable;
import org.springframework.data.relational.core.mapping.Table;

public class UserProfile {
    private Long userId;
    private String username;
    @Nullable
    private String email;

    public UserProfile() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
