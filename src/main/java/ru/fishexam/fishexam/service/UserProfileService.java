package ru.fishexam.fishexam.service;

import ru.fishexam.fishexam.dto.user.UserProfile;
import ru.fishexam.fishexam.dto.user.UserRole;

import java.time.LocalDate;


public class UserProfileService {
    private final TeacherService teacherService;
    private final StudentService studentService;

    public UserProfileService(TeacherService teacherService, StudentService studentService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    public UserProfile createBaseProfile(
            String username,
            String firstName,
            String patronymic,
            String phone,
            String email,
            LocalDate birth,
            String telegramId,
            UserRole role
    ) {
        return switch (role) {
            case STUDENT -> studentService.createBaseStudentProfile(
                    username,
                    firstName,
                    patronymic,
                    phone,
                    email,
                    birth,
                    telegramId
            );
            case TEACHER -> teacherService.createBaseTeacherProfile(
                    username,
                    firstName,
                    patronymic,
                    phone,
                    email,
                    birth,
                    telegramId
            );
        };
    }
}
