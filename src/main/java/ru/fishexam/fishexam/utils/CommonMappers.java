package ru.fishexam.fishexam.utils;

import ru.fishexam.fishexam.dto.StudentProfile;
import ru.fishexam.fishexam.dto.StudentProfileRequest;

public class CommonMappers {
    public static StudentProfile mapFromRequestStudentProfile(
            Long userId,
            StudentProfileRequest studentProfileRequest
    ) {
        var studentProfile = new StudentProfile();
        studentProfile.setUserId(userId);
        studentProfile.setName(studentProfileRequest.name());
        return studentProfile;
    }
}
