package ru.fishexam.fishexam.service;

import ru.fishexam.fishexam.dto.UserProfile;
import ru.fishexam.fishexam.dto.UserRole;

import java.time.LocalDate;
import java.util.Date;


public class UserProfileService {
    private final TeacherService teacherService;
    private final StudentService studentService;

    public UserProfileService(TeacherService teacherService, StudentService studentService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    public UserProfile createBaseProfile(Long userId, String username, String first_name, String patronymic,
                                         String phone, String email, LocalDate birth, String telegram_id, UserRole role) {
        return switch (role) {
            case STUDENT -> studentService.createBaseStudentProfile(userId, username, first_name, patronymic, phone,
                    email, birth, telegram_id);
            case TEACHER -> teacherService.createBaseTeacherProfile(userId, username, first_name, patronymic, phone,
                    email, birth, telegram_id);
        };
    }
}
