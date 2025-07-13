package ru.fishexam.fishexam.gigachat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fishexam.fishexam.gigachat.service.GigaChatService;

import java.io.IOException;

@RestController
@RequestMapping("/api/gigachat")
@RequiredArgsConstructor
public class GigaChatController {

    private final GigaChatService gigaChatService;

    @PostMapping("/ask")
    public ResponseEntity<String> askQuestion(@RequestBody String question) {
        try {
            String response = gigaChatService.sendMessage(question);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при подключении к GigaChat");
        }
    }
}