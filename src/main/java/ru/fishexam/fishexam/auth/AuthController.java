package ru.fishexam.fishexam.auth;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.fishexam.fishexam.auth.payload.request.LoginRequest;
import ru.fishexam.fishexam.auth.payload.request.SignupRequest;
import ru.fishexam.fishexam.auth.payload.request.TokenRefreshRequest;
import ru.fishexam.fishexam.auth.payload.response.JwtResponse;
import ru.fishexam.fishexam.auth.services.AuthService;
import ru.fishexam.fishexam.dto.UserProfile;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/api/auth/signin")
  public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok(
            authService.authenticateUser(loginRequest)
    );
  }

  @PostMapping("/api/auth/signup")
  public ResponseEntity<UserProfile> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    return ResponseEntity.ok(authService.registerUser(signUpRequest));
  }

  @PostMapping("/api/auth/refresh")
  public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
    return ResponseEntity.ok(authService.refreshToken(request));
  }
}
