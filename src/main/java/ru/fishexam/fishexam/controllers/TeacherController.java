package ru.fishexam.fishexam.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.fishexam.fishexam.dto.*;
import ru.fishexam.fishexam.service.TeacherService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class TeacherController {
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/api/profileTeach/{userId}")
    @PreAuthorize("@dataSecurityService.isOwner(#userId)")
    public ResponseEntity<UserProfile> userAccess(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(teacherService.getById(userId));
    }

    @PostMapping("/api/profileTeach/{userId}")
    @PreAuthorize("@dataSecurityService.isOwner(#userId)")
    public ResponseEntity<TeacherProfile> updateProfile(
            @PathVariable Long userId,
            @RequestBody TeacherProfileRequest teacherProfileRequest
    ) {
        return ResponseEntity.ok(teacherService.updateTeacherProfile(userId, teacherProfileRequest));
    }

    //Привязка ученика
    @PostMapping("/api/profile/{teacherId}/{studentId}")
    @PreAuthorize("@dataSecurityService.isOwner(#teacherId)")
    public ResponseEntity<TeacherStudentsRelations> assignStudent(
            @PathVariable Long teacherId,
            @PathVariable Long studentId
    ){
        return ResponseEntity.ok(teacherService.assignStudent(teacherId, studentId));
    }

    //Создание теоретических материалов
    @PostMapping("/api/outline/create/{teacherId}")
    @PreAuthorize("@dataSecurityService.isOwner(#teacherId)")
    public ResponseEntity<OutlineCreate> createOutline(
            @PathVariable Long teacherId,
            @RequestBody OutlineCreateRequest outlineCreateRequest
    ){
        return ResponseEntity.ok(teacherService.createOutline(teacherId, outlineCreateRequest));
    }

    //Отправка теоретических материалов ученику
    @PostMapping("/api/outline/post/{teacherId}/{studentId}/{outlineId}")
    @PreAuthorize("@dataSecurityService.isOwner(#teacherId)")
    public ResponseEntity<OutlineStudentRelation> assignOutline(
            @PathVariable Long teacherId,
            @PathVariable Long studentId,
            @PathVariable Long outlineId
    ){
        return ResponseEntity.ok(teacherService.assignOutline(studentId, outlineId));
    }

    //Создание задач для домашних работ
    @PostMapping("/api/homework/task/create/{teacherId}")
    @PreAuthorize("@dataSecurityService.isOwner(#teacherId)")
    public ResponseEntity<TaskModel> createTask(
            @PathVariable Long teacherId,
            @RequestBody TaskModelRequest taskModelRequest
    ){
        return ResponseEntity.ok(teacherService.createTask(teacherId, taskModelRequest));
    }

    //Создание домашних работ
    @PostMapping("/api/homework/create/{teacherId}")
    @PreAuthorize("@dataSecurityService.isOwner(#teacherId)")
    public ResponseEntity<HomeworkModel> createHomework(
            @PathVariable Long teacherId,
            @RequestBody HomeworkModelRequest homeworkModelRequest
    ){
        return ResponseEntity.ok(teacherService.createHomework(teacherId, homeworkModelRequest));
    }

    //Отправка домашних работ ученику
    @PostMapping("/api/task/post/{teacherId}/{studentId}/{homeworkId}")
    @PreAuthorize("@dataSecurityService.isOwner(#teacherId)")
    public ResponseEntity<HomeworkUserRelations> assignHomework(
            @PathVariable Long teacherId,
            @PathVariable Long studentId,
            @PathVariable Long homeworkId
    ){
        return ResponseEntity.ok(teacherService.assignHomework(studentId, homeworkId));
    }

    // Проверка работ
    @PostMapping("/api/task/check/{studentAnswersId}")
    public ResponseEntity<StudentAnswers> checkHomework(
            @PathVariable Long studentAnswersId,
            @RequestBody StudentAnswersRequest studentAnswersRequest
    ) {
        return ResponseEntity.ok(teacherService.checkHomework(studentAnswersId, studentAnswersRequest));
    }

    //Просмотр статистики всех прикрепленных учеников
    @GetMapping("/api/statistics/{teacherId}")
    @PreAuthorize("@dataSecurityService.isOwner(#teacherId)")
    public ResponseEntity<List<StudentStatistics>> getStudentsStatistics(
            @PathVariable Long teacherId
    ) {
        return ResponseEntity.ok(teacherService.getStudentsStatistics(teacherId));
    }

    //Генерация работы над ошибками для каждого ученика
    // позже
    @PostMapping("/api/error-correction/{teacherId}/{studentId}")
    @PreAuthorize("@dataSecurityService.isOwner(#teacherId)")
    public ResponseEntity<TaskModel> generateErrorCorrectionTask(
            @PathVariable Long teacherId,
            @PathVariable Long studentId) {
        return ResponseEntity.ok(teacherService.generateErrorCorrectionTask(teacherId, studentId));
    }

    //Геймификация сгенерированной работы над ошибками
    @PostMapping("/api/gamification/{teacherId}/{studentId}/{taskId}")
    @PreAuthorize("@dataSecurityService.isOwner(#teacherId)")
    public ResponseEntity<TaskModel> gamifyErrorCorrectionTask(
            @PathVariable Long teacherId,
            @PathVariable Long studentId,
            @PathVariable Long taskId) {
        return ResponseEntity.ok(teacherService.gamifyErrorCorrectionTask(studentId, taskId));
    }

    //Доступ к общей базе заданий по разным предметам - позже
}

