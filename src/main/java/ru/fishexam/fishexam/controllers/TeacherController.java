package ru.fishexam.fishexam.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.fishexam.fishexam.dto.common.VoidAnswer;
import ru.fishexam.fishexam.dto.homework.HomeworkModel;
import ru.fishexam.fishexam.dto.homework.HomeworkModelRequest;
import ru.fishexam.fishexam.dto.homework.HomeworkUserRelations;
import ru.fishexam.fishexam.dto.outline.OutlineCreate;
import ru.fishexam.fishexam.dto.outline.OutlineCreateRequest;
import ru.fishexam.fishexam.dto.outline.OutlineStudentRelation;
import ru.fishexam.fishexam.dto.student.StudentAnswers;
import ru.fishexam.fishexam.dto.student.StudentAnswersRequest;
import ru.fishexam.fishexam.dto.student.StudentProfile;
import ru.fishexam.fishexam.dto.student.StudentStatistics;
import ru.fishexam.fishexam.dto.task.TaskModel;
import ru.fishexam.fishexam.dto.task.TaskModelRequest;
import ru.fishexam.fishexam.dto.teacher.TeacherProfile;
import ru.fishexam.fishexam.dto.teacher.TeacherProfileRequest;
import ru.fishexam.fishexam.dto.teacher.TeacherStudentsRelations;
import ru.fishexam.fishexam.dto.user.UserProfile;
import ru.fishexam.fishexam.service.TeacherService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TeacherController {
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/api/teacher/profileTeach/{userId}")
    @PreAuthorize("@dataSecurityService.isOwner(#userId)")
    public ResponseEntity<UserProfile> userAccess(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(teacherService.getById(userId));
    }

    @PostMapping("/api/teacher/profileTeach/{userId}")
    @PreAuthorize("@dataSecurityService.isOwner(#userId)")
    public ResponseEntity<TeacherProfile> updateProfile(
            @PathVariable Long userId,
            @RequestBody TeacherProfileRequest teacherProfileRequest
    ) {
        return ResponseEntity.ok(teacherService.updateTeacherProfile(userId, teacherProfileRequest));
    }

    //Привязка ученика
    @PostMapping("/api/teacher/assign/{teacherId}/{studentId}")
    @PreAuthorize("@dataSecurityService.isOwner(#teacherId)")
    public ResponseEntity<VoidAnswer> assignStudent(
            @PathVariable Long teacherId,
            @PathVariable Long studentId
    ){
        teacherService.assignStudent(teacherId, studentId);
        return ResponseEntity.ok(new VoidAnswer("Success"));
    }

    //Создание теоретических материалов
    @PostMapping("/api/teacher/outline/create/{teacherId}")
    @PreAuthorize("@dataSecurityService.isOwner(#teacherId)")
    public ResponseEntity<OutlineCreate> createOutline(
            @PathVariable Long teacherId,
            @RequestBody OutlineCreateRequest outlineCreateRequest
    ){
        return ResponseEntity.ok(teacherService.createOutline(teacherId, outlineCreateRequest));
    }

    //Отправка теоретических материалов ученику
    @PostMapping("/api/teacher/outline/post/{teacherId}/{studentId}/{outlineId}")
    @PreAuthorize("@dataSecurityService.isOwner(#teacherId)")
    public ResponseEntity<OutlineCreate> assignOutline(
            @PathVariable Long teacherId,
            @PathVariable Long studentId,
            @PathVariable Long outlineId
    ){
        return ResponseEntity.ok(teacherService.assignOutline(studentId, outlineId));
    }

    //Создание задач для домашних работ
    @PostMapping("/api/teacher/homework/task/create/{teacherId}")
    @PreAuthorize("@dataSecurityService.isOwner(#teacherId)")
    public ResponseEntity<TaskModel> createTask(
            @PathVariable Long teacherId,
            @RequestBody TaskModelRequest taskModelRequest
    ){
        return ResponseEntity.ok(teacherService.createTask(teacherId, taskModelRequest));
    }

    //Создание домашних работ
    @PostMapping("/api/teacher/homework/create/{teacherId}")
    @PreAuthorize("@dataSecurityService.isOwner(#teacherId)")
    public ResponseEntity<HomeworkModel> createHomework(
            @PathVariable Long teacherId,
            @RequestBody HomeworkModelRequest homeworkModelRequest
    ){
        return ResponseEntity.ok(teacherService.createHomework(teacherId, homeworkModelRequest));
    }

    //Отправка домашних работ ученику
    @PostMapping("/api/teacher/task/post/{teacherId}/{studentId}/{homeworkId}")
    @PreAuthorize("@dataSecurityService.isOwner(#teacherId)")
    public ResponseEntity<HomeworkModel> assignHomework(
            @PathVariable Long teacherId,
            @PathVariable Long studentId,
            @PathVariable Long homeworkId
    ){
        return ResponseEntity.ok(teacherService.assignHomework(studentId, homeworkId));
    }

    // Проверка работ
    @PostMapping("/api/teacher/task/check/{studentAnswersId}")
    public ResponseEntity<StudentAnswers> checkHomework(
            @PathVariable Long studentAnswersId,
            @RequestBody StudentAnswersRequest studentAnswersRequest
    ) {
        return ResponseEntity.ok(teacherService.checkHomework(studentAnswersId, studentAnswersRequest));
    }

    //Просмотр статистики всех прикрепленных учеников
    @GetMapping("/api/teacher/statistics/{teacherId}")
    @PreAuthorize("@dataSecurityService.isOwner(#teacherId)")
    public ResponseEntity<List<StudentStatistics>> getStudentsStatistics(
            @PathVariable Long teacherId
    ) {
        return ResponseEntity.ok(teacherService.getStudentsStatistics(teacherId));
    }

    //Генерация работы над ошибками для каждого ученика
    // позже
    @PostMapping("/api/teacher/error-correction/{teacherId}/{studentId}")
    @PreAuthorize("@dataSecurityService.isOwner(#teacherId)")
    public ResponseEntity<TaskModel> generateErrorCorrectionTask(
            @PathVariable Long teacherId,
            @PathVariable Long studentId) {
        return ResponseEntity.ok(teacherService.generateErrorCorrectionTask(teacherId, studentId));
    }

    //Геймификация сгенерированной работы над ошибками
    @PostMapping("/api/teacher/gamification/{teacherId}/{studentId}/{taskId}")
    @PreAuthorize("@dataSecurityService.isOwner(#teacherId)")
    public ResponseEntity<TaskModel> gamifyErrorCorrectionTask(
            @PathVariable Long teacherId,
            @PathVariable Long studentId,
            @PathVariable Long taskId) {
        return ResponseEntity.ok(teacherService.gamifyErrorCorrectionTask(studentId, taskId));
    }

    //Доступ к общей базе заданий по разным предметам - позже
}

