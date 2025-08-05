package ru.fishexam.fishexam.gigachat.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.fishexam.fishexam.gigachat.exHandler.GigaChatException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class GigaChatAuthService {

    private static final Logger log = LoggerFactory.getLogger(GigaChatAuthService.class);
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    private static final String GRANT_TYPE = "client_credentials";
    private static final String SCOPE = "GIGACHAT_API_PERS";

    @Value("${gigachat.client.id}")
    private String clientId;
    @Value("${gigachat.client.secret}")
    private String clientSecret;
    @Value("${gigachat.client.authUrl}")
    private String authUrl;
    @Value("${gigachat.client.rqUid}")
    private String rqUid;

    public GigaChatAuthService(CloseableHttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public String getAccessToken() throws Exception {
        var request = createTokenRequest();
        return executeTokenRequest(request);
    }

    private HttpPost createTokenRequest() {
        HttpPost request = new HttpPost(authUrl);
        request.setHeader("Authorization", buildBasicAuthHeader());
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
        request.setHeader("Accept", "application/json");
        request.setHeader("RqUID", rqUid);
        request.setEntity(new StringEntity(buildPayload(), StandardCharsets.UTF_8));
        return request;
    }

    private String buildBasicAuthHeader() {
        String credentials = String.format(
                "%s:%s",
                clientId,
                clientSecret
        );
        return String.format(
                "Basic  %s",
                Base64.getEncoder().encodeToString(credentials.getBytes())
        );
    }

    private String buildPayload() {
        return "scope=" + SCOPE + "&grant_type=" + GRANT_TYPE;
    }

    private String executeTokenRequest(HttpPost request) throws Exception {
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            GigaChatUtils.validateResponse(log, response);
            return parseTokenFromResponse(response);
        }
    }


    private String parseTokenFromResponse(CloseableHttpResponse response) throws Exception {
        String json = EntityUtils.toString(response.getEntity());
        JsonNode node = objectMapper.readTree(json);
        JsonNode tokenNode = node.get("access_token");
        if (tokenNode == null || tokenNode.isNull()) {
            throw new GigaChatException(String.format("Access token not found in response: %s",  json));
        }
        return tokenNode.asText();
    }
}