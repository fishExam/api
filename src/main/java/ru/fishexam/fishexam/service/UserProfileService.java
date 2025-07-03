package ru.fishexam.fishexam.service;

import ru.fishexam.fishexam.dto.UserProfile;
import ru.fishexam.fishexam.dto.UserRole;


public class UserProfileService {
    private final TeacherService teacherService;
    private final StudentService studentService;

    public UserProfileService(TeacherService teacherService, StudentService studentService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    public UserProfile createBaseProfile(Long id, String username, UserRole role) {
        return switch (role) {
            case STUDENT -> studentService.createBaseStudentProfile(id, username);
            case TEACHER -> teacherService.createBaseTeacherProfile(id, username);
        };
    }
}
