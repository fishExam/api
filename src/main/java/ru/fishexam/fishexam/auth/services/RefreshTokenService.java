package ru.fishexam.fishexam.auth.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import ru.fishexam.fishexam.auth.models.RefreshToken;
import ru.fishexam.fishexam.auth.dao.RefreshTokenDao;

public class RefreshTokenService {
    private Long refreshTokenDurationMs = 86400000L;

    private final RefreshTokenDao refreshTokenDao;

    public RefreshTokenService(RefreshTokenDao refreshTokenDao) {
        this.refreshTokenDao = refreshTokenDao;
    }

    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = new RefreshToken(
                username,
                UUID.randomUUID().toString(),
                Instant.now().plusMillis(refreshTokenDurationMs)
        );

        refreshTokenDao.save(refreshToken);

        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenDao.deleteByUser(token.getUsername());
            throw new RuntimeException(token.getToken() + ": Refresh token was expired.");
        }

        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenDao.findByToken(token);
    }
}

