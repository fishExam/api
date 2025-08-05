package ru.fishexam.fishexam.utils;

import ru.fishexam.fishexam.dto.homework.HomeworkModel;
import ru.fishexam.fishexam.dto.homework.HomeworkModelRequest;
import ru.fishexam.fishexam.dto.outline.OutlineCreate;
import ru.fishexam.fishexam.dto.outline.OutlineCreateRequest;
import ru.fishexam.fishexam.dto.student.StudentProfile;
import ru.fishexam.fishexam.dto.student.StudentProfileRequest;
import ru.fishexam.fishexam.dto.task.TaskModel;
import ru.fishexam.fishexam.dto.task.TaskModelRequest;
import ru.fishexam.fishexam.dto.teacher.TeacherProfile;
import ru.fishexam.fishexam.dto.teacher.TeacherProfileRequest;
import ru.fishexam.fishexam.dto.user.UserProfile;

public class CommonMappers {
    public static StudentProfile mapFromRequestStudentProfile(
            Long userId,
            StudentProfileRequest studentProfileRequest
    ) {
        return new StudentProfile(
                userId,
                studentProfileRequest.username(),
                studentProfileRequest.firstName(),
                studentProfileRequest.patronymic(),
                studentProfileRequest.phone(),
                studentProfileRequest.email(),
                studentProfileRequest.birth(),
                studentProfileRequest.telegramId(),
                studentProfileRequest.parentId(),
                studentProfileRequest.tasksCount()
        );
    }

    private static UserProfile mergeTwoUserProfile(
            UserProfile oldUserProfile,
            UserProfile newUserProfile
    ) {
        if (newUserProfile.getUsername() != null) {
            oldUserProfile.setUsername(newUserProfile.getUsername());
        }

        if (newUserProfile.getFirstName() != null) {
            oldUserProfile.setFirstName(newUserProfile.getFirstName());
        }

        if (newUserProfile.getPatronymic() != null) {
            oldUserProfile.setPatronymic(newUserProfile.getPatronymic());
        }
        if (newUserProfile.getPhone() != null) {
            oldUserProfile.setPhone(newUserProfile.getPhone());
        }

        if (newUserProfile.getEmail() != null) {
            oldUserProfile.setEmail(newUserProfile.getEmail());
        }

        if (newUserProfile.getBirth() != null) {
            oldUserProfile.setBirth(newUserProfile.getBirth());
        }
        if (newUserProfile.getTelegramId() != null) {
            oldUserProfile.setTelegramId(newUserProfile.getTelegramId());
        }

        return oldUserProfile;
    }

    public static StudentProfile mergeTwoStudentProfile(
            StudentProfile oldStudentProfile,
            StudentProfile newStudentProfile
    ) {
        oldStudentProfile = (StudentProfile) mergeTwoUserProfile(oldStudentProfile, newStudentProfile);

        if (newStudentProfile.getParentId() != null) {
            oldStudentProfile.setParentId(newStudentProfile.getParentId());
        }

        if (newStudentProfile.getTasksCount() != 0) {
            oldStudentProfile.setTasksCount(newStudentProfile.getTasksCount());
        }

        return oldStudentProfile;
    }

    public static TeacherProfile mapFromRequestTeacherProfile(
            Long userId,
            TeacherProfileRequest teacherProfileRequest
    ) {
        return new TeacherProfile(
                userId,
                teacherProfileRequest.username(),
                teacherProfileRequest.firstName(),
                teacherProfileRequest.patronymic(),
                teacherProfileRequest.phone(),
                teacherProfileRequest.email(),
                teacherProfileRequest.birth(),
                teacherProfileRequest.telegramId()
        );
    }

    public static TeacherProfile mergeTwoTeacherProfile(
            TeacherProfile oldTeacherProfile,
            TeacherProfile newTeacherProfile
    ) {
        return (TeacherProfile) mergeTwoUserProfile(oldTeacherProfile, newTeacherProfile);
    }
    public static OutlineCreate mapFromRequestOutlineCreate(
            Long outlineId,
            Long teacherId,
            OutlineCreateRequest outlineCreateRequest
    ) {
        return new OutlineCreate(outlineId, teacherId, outlineCreateRequest.title());
    }

    public static OutlineCreate mergeTwoOutlines(
            OutlineCreate oldOutline,
            OutlineCreate newOutline
    ) {
        if (newOutline.getTitle() != null) {
            oldOutline.setTitle(newOutline.getTitle());
        }
        return oldOutline;
    }

    public static HomeworkModel mapFromRequestHomework(
            Long authorId,
            HomeworkModelRequest homeworkModelRequest
    ) {
        var homeworkModel = new HomeworkModel();
        homeworkModel.setAuthorId(authorId);
        homeworkModel.setDescription(homeworkModelRequest.description());
        return homeworkModel;
    }

    public static HomeworkModel mergeTwoHomeworkss(
            HomeworkModel oldHomeworkModel,
            HomeworkModel newHomeworkModel
    ) {
        if (newHomeworkModel.getDescription() != null) {
            oldHomeworkModel.setDescription(newHomeworkModel.getDescription());
        }
        return oldHomeworkModel;
    }

    public static TaskModel mapFromRequestTaskModel(
            Long taskId,
            Long authorId,
            TaskModelRequest taskModelRequest
    ) {
        var taskModel = new TaskModel(taskId, authorId,
                taskModelRequest.title(),
                taskModelRequest.answer());
        return taskModel;
    }

    public static TaskModel mergeTwoTasks(
            TaskModel oldTaskModel,
            TaskModel newTaskModel
    ) {
        if (newTaskModel.getTitle() != null) {
            oldTaskModel.setTitle(newTaskModel.getTitle());
        }
        if (newTaskModel.getAnswer() != null) {
            oldTaskModel.setAnswer(newTaskModel.getAnswer());
        }
        return oldTaskModel;
    }
}
