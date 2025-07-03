package ru.fishexam.fishexam.auth.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ru.fishexam.fishexam.auth.models.RefreshToken;
import ru.fishexam.fishexam.auth.models.UserDetailsImpl;
import ru.fishexam.fishexam.auth.payload.request.LoginRequest;
import ru.fishexam.fishexam.auth.payload.request.SignupRequest;
import ru.fishexam.fishexam.auth.payload.request.TokenRefreshRequest;
import ru.fishexam.fishexam.auth.payload.response.JwtResponse;
import ru.fishexam.fishexam.auth.dao.UserDao;
import ru.fishexam.fishexam.auth.security.JwtUtils;
import ru.fishexam.fishexam.dto.UserProfile;
import ru.fishexam.fishexam.service.UserProfileService;

public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDao userDao;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserProfileService userProfileService;

    public AuthService(AuthenticationManager authenticationManager, UserDao userDao, JwtUtils jwtUtils, RefreshTokenService refreshTokenService, PasswordEncoder passwordEncoder, UserProfileService userProfileService) {
        this.authenticationManager = authenticationManager;
        this.userDao = userDao;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
        this.userProfileService = userProfileService;
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        var refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

        return new JwtResponse(
                jwt,
                userDetails.getUsername(),
                userDetails.getUserId(),
                refreshToken.getToken()
        );
    }

    public UserProfile registerUser(SignupRequest signUpRequest) {
        if (userDao.existsByUsername(signUpRequest.username())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        String hashedPassword = passwordEncoder.encode(signUpRequest.password());

        var saved = userDao.save(signUpRequest.username(), hashedPassword);
        return userProfileService.createBaseProfile(saved.getUserid(), saved.getUsername(), signUpRequest.userRole());
    }

    public JwtResponse refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.refreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUsername)
                .map(username -> {
                    String token = jwtUtils.generateTokenFromUsername(username);
                    var user = userDao.findByUsername(username).orElseThrow();

                    return new JwtResponse(
                            token,
                            username,
                            user.getUserid(),
                            requestRefreshToken
                    );
                })
                .orElseThrow(() -> new RuntimeException(requestRefreshToken + "Refresh token not found!"));
    }
}
