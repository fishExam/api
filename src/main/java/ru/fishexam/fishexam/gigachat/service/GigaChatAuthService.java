package ru.fishexam.fishexam.gigachat.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;

@Service
public class GigaChatAuthService {

    private final CloseableHttpClient httpClient;
    @Value("${gigachat.client.id}")
    private String clientId;
    @Value("${gigachat.client.secret}")
    private String clientSecret;
    private final String authUrl = "https://ngw.devices.sberbank.ru:9443/api/v2/oauth";
    private final String rqUid = "26bb413d-ea81-4856-8fb3-6ec25ae3f0d1"; // Уникальный идентификатор

    public GigaChatAuthService(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String getAccessToken() throws IOException {
        HttpPost request = new HttpPost(authUrl);

        String auth = Base64.getEncoder()
                .encodeToString((clientId + ":" + clientSecret).getBytes());

        request.setHeader("Authorization", "Basic " + auth);
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        request.setHeader("Accept", "application/json");
        request.setHeader("RqUID", rqUid);

        // Параметры запроса
        String payload = "scope=GIGACHAT_API_PERS&grant_type=client_credentials";
        request.setEntity(new StringEntity(payload));

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            String json = EntityUtils.toString(response.getEntity());
            System.out.println("GigaChat API Response: " + json);

            if (statusCode != 200) {
                throw new IOException("Failed to get access token. HTTP Status: " + statusCode + ", Body: " + json);
            }

            JsonNode node = new ObjectMapper().readTree(json);
            JsonNode tokenNode = node.get("access_token");
            if (tokenNode == null || tokenNode.isNull()) {
                throw new IllegalStateException("Access token not found in response: " + json);
            }
            return tokenNode.asText();
        }
    }
}