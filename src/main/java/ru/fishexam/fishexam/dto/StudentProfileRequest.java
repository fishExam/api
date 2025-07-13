package ru.fishexam.fishexam.dto;

import java.time.LocalDate;
import java.util.Date;

public record StudentProfileRequest(
        String surname, String firstName, String patronymic, String phone,
        String email, LocalDate birth, String telegramId, String parentId, int tasksCount
) {
}
