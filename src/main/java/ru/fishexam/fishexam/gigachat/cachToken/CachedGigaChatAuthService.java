package ru.fishexam.fishexam.gigachat.cachToken;

import org.springframework.stereotype.Service;
import ru.fishexam.fishexam.gigachat.service.GigaChatAuthService;

import java.io.IOException;

@Service
public class CachedGigaChatAuthService {

    private final GigaChatAuthService authService;
    private String cachedToken;
    private long tokenExpiresAt;

    public CachedGigaChatAuthService(GigaChatAuthService authService) {
        this.authService = authService;
    }

    public synchronized String getAccessToken() throws IOException {
        if (cachedToken == null || System.currentTimeMillis() > tokenExpiresAt) {
            cachedToken = authService.getAccessToken();
            tokenExpiresAt = System.currentTimeMillis() + 50 * 60 * 1000; // 50 минут
        }
        return cachedToken;
    }
}