package ru.fishexam.fishexam.auth.payload.request;

import jakarta.validation.constraints.*;
import ru.fishexam.fishexam.dto.user.UserRole;

import java.time.LocalDate;

public record SignupRequest(
        @NotBlank
        String username,
        @NotBlank
        String firstName,
        @NotBlank
        String patronymic,
        @NotBlank
        String phone,
        @NotBlank
        String email,
        @NotNull
        @Past
        LocalDate birth,
        @NotBlank
        String telegramId,
        @NotBlank
        String password,
        @NotNull
        UserRole userRole
) {
}
