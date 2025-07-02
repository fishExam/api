package ru.fishexam.fishexam.service;

import ru.fishexam.fishexam.dao.TeacherDao;
import ru.fishexam.fishexam.dto.TeacherProfile;

public class TeacherService {

    private final TeacherDao teacherDao;

    public TeacherService(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    public TeacherProfile createBaseTeacherProfile(Long userId, String username) {
        TeacherProfile teacherProfile = new TeacherProfile();
        teacherProfile.setUserId(userId);
        teacherProfile.setUsername(username);
        teacherDao.update(teacherProfile);
        return teacherProfile;
    }
}
