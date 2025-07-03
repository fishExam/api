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

    public static StudentProfile mergeTwoStudentProfile(
            StudentProfile oldStudentProfile,
            StudentProfile newStudentProfile
    ) {
        if (newStudentProfile.getUsername() != null) {
            oldStudentProfile.setUsername(newStudentProfile.getUsername());
        }

        if (newStudentProfile.getEmail() != null) {
            oldStudentProfile.setEmail(newStudentProfile.getEmail());
        }

        if (newStudentProfile.getName() != null) {
            oldStudentProfile.setName(newStudentProfile.getName());
        }

        return oldStudentProfile;
    }
}
