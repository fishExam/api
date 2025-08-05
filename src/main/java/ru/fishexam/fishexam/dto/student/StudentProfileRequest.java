package ru.fishexam.fishexam.dto.student;

import java.time.LocalDate;

public record StudentProfileRequest(
        String username, String firstName, String patronymic, String phone,
        String email, LocalDate birth, String telegramId, String parentId, int tasksCount
) {
}
