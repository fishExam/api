package ru.fishexam.fishexam.gigachat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import ru.fishexam.fishexam.gigachat.dto.GigaChatRequest;
import ru.fishexam.fishexam.gigachat.dto.GigaChatMessage;
import ru.fishexam.fishexam.gigachat.exHandler.GigaChatException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class GigaChatService {

    private final CloseableHttpClient httpClient;
    private final GigaChatAuthService authService;
    private final ObjectMapper objectMapper;
    private final String apiUrl;

    private static final String COMPLETIONS_ENDPOINT = "/completions";
    private static final String AUTH_HEADER_TEMPLATE = "Bearer %s";

    public GigaChatService(
            CloseableHttpClient httpClient,
            GigaChatAuthService authService,
            ObjectMapper objectMapper,
            String apiUrl
    ) {
        this.httpClient = httpClient;
        this.authService = authService;
        this.objectMapper = objectMapper;
        this.apiUrl = apiUrl;
    }

    public String sendMessage(String prompt) throws Exception {
        var token = authService.getAccessToken();
        var request = createRequest(token, prompt);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            GigaChatUtils.validateResponse(log, response);
            return extractContentFromResponse(response);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private HttpPost createRequest(String token, String prompt) throws JsonProcessingException, UnsupportedEncodingException {
        HttpPost request = new HttpPost(apiUrl + COMPLETIONS_ENDPOINT);
        request.setHeader("Authorization", String.format(AUTH_HEADER_TEMPLATE, token));
        request.setHeader("Content-Type", "application/json");
        request.setEntity(createRequestBody(prompt));
        return request;
    }

    private StringEntity createRequestBody(String prompt) throws JsonProcessingException, UnsupportedEncodingException {
        GigaChatRequest requestBody = new GigaChatRequest(
                "GigaChat",
                List.of(new GigaChatMessage("user", prompt))
        );

        return new StringEntity(objectMapper.writeValueAsString(requestBody));
    }

    private String extractContentFromResponse(CloseableHttpResponse response) throws Exception {
        String jsonResponse = EntityUtils.toString(response.getEntity());
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        return rootNode.path("choices")
                .path(0)
                .path("message")
                .path("content")
                .asText();
    }
}