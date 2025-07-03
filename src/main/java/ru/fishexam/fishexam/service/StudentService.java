package ru.fishexam.fishexam.service;

import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(transactionManager = "mainDbTransactionManager", rollbackFor = Throwable.class)
    public StudentProfile updateStudentProfile(Long userId, StudentProfileRequest studentProfileRequest) {
        var newProfile = CommonMappers.mapFromRequestStudentProfile(userId, studentProfileRequest);
        var oldProfile = getById(userId);

        var updateProfile = CommonMappers.mergeTwoStudentProfile(oldProfile, newProfile);

        studentDao.update(updateProfile);

        return updateProfile;
    }

    public StudentProfile getById(Long userId) {
        return studentDao.getById(userId).orElseThrow();
    }
}
