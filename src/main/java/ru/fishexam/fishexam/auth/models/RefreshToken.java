package ru.fishexam.fishexam.auth.models;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class RefreshToken {
    private String username;

    private String token;

    private Instant expiryDate;
}
