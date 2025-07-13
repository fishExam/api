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

@Service
public class GigaChatService {

    private final CloseableHttpClient httpClient;
    private final GigaChatAuthService authService;
    private final String apiUrl;

    public GigaChatService(CloseableHttpClient httpClient, GigaChatAuthService authService, String apiUrl) {
        this.httpClient = httpClient;
        this.authService = authService;
        this.apiUrl = apiUrl;
    }

    public String sendMessage(String prompt) throws IOException {
        String token = authService.getAccessToken();

        HttpPost request = new HttpPost(apiUrl + "/completions");
        request.setHeader("Authorization", "Bearer " + token);
        request.setHeader("Content-Type", "application/json");

        String requestBody = String.format(
                "{\"model\":\"GigaChat\", \"messages\":[{\"role\":\"user\",\"content\":\"%s\"}]}",
                prompt);

        request.setEntity(new StringEntity(requestBody));

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String jsonResponse = EntityUtils.toString(response.getEntity());
            JsonNode node = new ObjectMapper().readTree(jsonResponse);
            return node.get("choices").get(0).get("message").get("content").asText();
        }
    }
}