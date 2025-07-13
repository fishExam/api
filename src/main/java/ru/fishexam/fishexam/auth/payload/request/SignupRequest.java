package ru.fishexam.fishexam.auth.payload.request;

import jakarta.validation.constraints.*;
import ru.fishexam.fishexam.dto.UserRole;

import java.time.LocalDate;

public record SignupRequest(
        @NotBlank
        String username,
        @NotBlank
        String first_name,
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
        String telegram_id,
        @NotBlank
        String password,
        @NotNull
        UserRole userRole
) {
}
