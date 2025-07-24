package ru.fishexam.fishexam.dto.teacher;

import jakarta.annotation.Nullable;
import ru.fishexam.fishexam.dto.user.UserProfile;

import java.time.LocalDate;

public class TeacherProfile extends UserProfile {
    public TeacherProfile(Long userId, String surname, String firstName, @Nullable String patronymic, String phone, @Nullable String email, LocalDate birth, String telegramId) {
        super(userId, surname, firstName, patronymic, phone, email, birth, telegramId);
    }
}
