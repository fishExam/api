package ru.fishexam.fishexam.service;

import ru.fishexam.fishexam.dao.StudentDao;
import ru.fishexam.fishexam.dto.StudentProfile;
import ru.fishexam.fishexam.dto.StudentProfileRequest;
import ru.fishexam.fishexam.utils.CommonMappers;

public class StudentService {

    private final StudentDao studentDao;

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public StudentProfile createBaseStudentProfile(Long userId, String username) {
        StudentProfile studentProfile = new StudentProfile();
        studentProfile.setUserId(userId);
        studentProfile.setUsername(username);
        studentDao.update(studentProfile);
        return studentProfile;
    }

    public StudentProfile updateStudentProfile(Long userId, StudentProfileRequest studentProfileRequest) {
        var studentProfile = CommonMappers.mapFromRequestStudentProfile(userId, studentProfileRequest);
        studentDao.update(studentProfile);
        return getById(userId);
    }

    public StudentProfile getById(Long userId) {
        return studentDao.getById(userId).orElseThrow();
    }
}
