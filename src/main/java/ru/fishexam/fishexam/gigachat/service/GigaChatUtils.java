package ru.fishexam.fishexam.gigachat.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import ru.fishexam.fishexam.gigachat.exHandler.GigaChatException;

public class GigaChatUtils {

    public static void validateResponse(
            Logger log,
            CloseableHttpResponse response
    ) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode < 200 || statusCode >= 300) {
            log.error(
                    "Problem with GigaChat api. Status code: {}, body: {}",
                    statusCode,
                    EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8)
            );
            throw new GigaChatException(String.format("Unexpected response status from gigachat api: %s",  + statusCode));
        }
    }
}
