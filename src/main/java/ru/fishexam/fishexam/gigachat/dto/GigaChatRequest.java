package ru.fishexam.fishexam.gigachat.dto;

import java.util.List;

public record GigaChatRequest(
        String model,
        List<GigaChatMessage> gigaChatMessages
) {}
