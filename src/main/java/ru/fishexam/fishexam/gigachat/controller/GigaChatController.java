package ru.fishexam.fishexam.gigachat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.fishexam.fishexam.gigachat.service.GigaChatService;

@RestController
public class GigaChatController {

    private final GigaChatService gigaChatService;

    public GigaChatController(GigaChatService gigaChatService) {
        this.gigaChatService = gigaChatService;
    }

    @PostMapping("/api/gigachat/ask")
    public ResponseEntity<String> askQuestion(@RequestBody String question)
            throws Exception {
        String response = gigaChatService.sendMessage(question);
        return ResponseEntity.ok(response);
    }
}