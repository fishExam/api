package ru.fishexam.fishexam.auth.payload.request;

import jakarta.validation.constraints.*;
import ru.fishexam.fishexam.dto.UserRole;

public record SignupRequest(
        @NotBlank
        String username,
        @NotBlank
        String password,
        @NotNull
        UserRole userRole
) {
}
