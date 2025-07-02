package ru.fishexam.fishexam.auth.payload.request;

import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequest(
        @NotBlank
        String refreshToken
){
}
