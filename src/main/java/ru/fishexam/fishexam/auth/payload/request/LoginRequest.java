package ru.fishexam.fishexam.auth.payload.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
		@NotBlank
		String surname,
		@NotBlank
		String password
) {
}
