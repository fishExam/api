package ru.fishexam.fishexam.utils;

import ru.fishexam.fishexam.dto.hobby.HobbyModel;
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

public class CommonMappers {
    public static StudentProfile mapFromRequestStudentProfile(
            Long userId,
            StudentProfileRequest studentProfileRequest
    ) {
        var studentProfile = new StudentProfile(userId,
                studentProfileRequest.surname(),
                studentProfileRequest.firstName(),
                studentProfileRequest.patronymic(),
                studentProfileRequest.phone(),
                studentProfileRequest.email(),
                studentProfileRequest.birth(),
                studentProfileRequest.telegramId(),
                studentProfileRequest.parentId(),
                studentProfileRequest.tasksCount());
        return studentProfile;
    }

    public static StudentProfile mergeTwoStudentProfile(
            StudentProfile oldStudentProfile,
            StudentProfile newStudentProfile
    ) {
        if (newStudentProfile.getSurname() != null) {
            oldStudentProfile.setSurname(newStudentProfile.getSurname());
        }

        if (newStudentProfile.getFirstName() != null) {
            oldStudentProfile.setFirstName(newStudentProfile.getFirstName());
        }

        if (newStudentProfile.getPatronymic() != null) {
            oldStudentProfile.setPatronymic(newStudentProfile.getPatronymic());
        }
        if (newStudentProfile.getPhone() != null) {
            oldStudentProfile.setPhone(newStudentProfile.getPhone());
        }

        if (newStudentProfile.getEmail() != null) {
            oldStudentProfile.setEmail(newStudentProfile.getEmail());
        }

        if (newStudentProfile.getBirth() != null) {
            oldStudentProfile.setBirth(newStudentProfile.getBirth());
        }
        if (newStudentProfile.getTelegramId() != null) {
            oldStudentProfile.setTelegramId(newStudentProfile.getTelegramId());
        }

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
        var teacherProfile = new TeacherProfile(userId,
                teacherProfileRequest.surname(),
                teacherProfileRequest.firstName(),
                teacherProfileRequest.patronymic(),
                teacherProfileRequest.phone(),
                teacherProfileRequest.email(),
                teacherProfileRequest.birth(),
                teacherProfileRequest.telegramId());
        return teacherProfile;
    }

    public static TeacherProfile mergeTwoTeacherProfile(
            TeacherProfile oldTeacherProfile,
            TeacherProfile newTeacherProfile
    ) {
        if (newTeacherProfile.getSurname() != null) {
            oldTeacherProfile.setSurname(newTeacherProfile.getSurname());
        }

        if (newTeacherProfile.getFirstName() != null) {
            oldTeacherProfile.setFirstName(newTeacherProfile.getFirstName());
        }

        if (newTeacherProfile.getPatronymic() != null) {
            oldTeacherProfile.setPatronymic(newTeacherProfile.getPatronymic());
        }
        if (newTeacherProfile.getPhone() != null) {
            oldTeacherProfile.setPhone(newTeacherProfile.getPhone());
        }

        if (newTeacherProfile.getEmail() != null) {
            oldTeacherProfile.setEmail(newTeacherProfile.getEmail());
        }

        if (newTeacherProfile.getBirth() != null) {
            oldTeacherProfile.setBirth(newTeacherProfile.getBirth());
        }
        if (newTeacherProfile.getTelegramId() != null) {
            oldTeacherProfile.setTelegramId(newTeacherProfile.getTelegramId());
        }

        return oldTeacherProfile;
    }
    public static OutlineCreate mapFromRequestOutlineCreate(
            Long outlineId,
            Long teacherId,
            OutlineCreateRequest outlineCreateRequest
    ) {
        var outlineCreate = new OutlineCreate(outlineId, teacherId,
        outlineCreateRequest.title());
        return outlineCreate;
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
