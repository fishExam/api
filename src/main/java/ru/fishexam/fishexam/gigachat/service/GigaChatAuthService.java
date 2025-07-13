package ru.fishexam.fishexam.gigachat.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

@Service
public class GigaChatAuthService {

    private final CloseableHttpClient httpClient;
    private final String clientId;
    private final String clientSecret;
    private final String authUrl;

    public GigaChatAuthService(CloseableHttpClient httpClient, String clientId, String clientSecret, String authUrl) {
        this.httpClient = httpClient;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authUrl = authUrl;
    }

    public String getAccessToken() throws IOException {
        HttpPost request = new HttpPost(authUrl);

        String auth = Base64.getEncoder()
                .encodeToString((clientId + ":" + clientSecret).getBytes());

        request.setHeader("Authorization", "Basic " + auth);
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        request.setEntity(new StringEntity("grant_type=client_credentials"));

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String json = EntityUtils.toString(response.getEntity());
            JsonNode node = new ObjectMapper().readTree(json);
            return node.get("access_token").asText();
        }
    }
}