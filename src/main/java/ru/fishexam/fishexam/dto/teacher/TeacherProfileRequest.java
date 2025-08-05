package ru.fishexam.fishexam.dto.teacher;

import java.time.LocalDate;

public record TeacherProfileRequest (
        String username, String firstName, String patronymic, String phone,
        String email, LocalDate birth, String telegramId)
{
}
