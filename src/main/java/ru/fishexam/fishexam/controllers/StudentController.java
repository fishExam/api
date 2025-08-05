package ru.fishexam.fishexam.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.fishexam.fishexam.dto.hobby.HobbyModel;
import ru.fishexam.fishexam.dto.hobby.HobbyModelRequest;
import ru.fishexam.fishexam.dto.hobby.HobbyStudentRelations;
import ru.fishexam.fishexam.dto.homework.HomeworkModel;
import ru.fishexam.fishexam.dto.student.StudentAnswers;
import ru.fishexam.fishexam.dto.student.StudentAnswersRequest;
import ru.fishexam.fishexam.dto.student.StudentProfile;
import ru.fishexam.fishexam.dto.student.StudentProfileRequest;
import ru.fishexam.fishexam.dto.user.UserProfile;
import ru.fishexam.fishexam.service.StudentService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class StudentController {
  private final StudentService studentService;

  public StudentController(StudentService studentService) {
      this.studentService = studentService;
  }

  @GetMapping("/api/student/profile/{userId}")
  @PreAuthorize("@dataSecurityService.isOwner(#userId)")
  public ResponseEntity<StudentProfile> getStudentProfile(
          @PathVariable Long userId
  ) {
    return ResponseEntity.ok(studentService.getById(userId));
  }

  @PostMapping("/api/student/profile/{userId}")
  @PreAuthorize("@dataSecurityService.isOwner(#userId)")
  public ResponseEntity<StudentProfile> updateProfile(
          @PathVariable Long userId,
          @RequestBody StudentProfileRequest studentProfileRequest
  ) {
    return ResponseEntity.ok(studentService.updateStudentProfile(userId, studentProfileRequest));
  }

  @PostMapping("/api/student/hobby/{userId}")
  @PreAuthorize("@dataSecurityService.isOwner(#userId)")
  public ResponseEntity<List<HobbyModel>> createHobby(
          @PathVariable Long userId,
          @RequestBody HobbyModelRequest hobbyModelRequest
  ) {
    return ResponseEntity.ok(studentService.createHobby(userId, hobbyModelRequest));
  }

  // Просмотр заданных домашних заданий
  @GetMapping("/api/student/homework/{userId}")
  @PreAuthorize("@dataSecurityService.isOwner(#userId)")
  public ResponseEntity<List<HomeworkModel>> getAssignedHomeworks(@PathVariable Long userId) {
    return ResponseEntity.ok(studentService.getAssignedHomeworks(userId));
  }

  // Просмотр собственной статистики по выполненным домашним заданиям
  @GetMapping("/api/student/statistic/{userId}")
  @PreAuthorize("@dataSecurityService.isOwner(#userId)")
  public ResponseEntity<StudentProfile> getStudentStatistics(@PathVariable Long userId) {
    return ResponseEntity.ok(studentService.getStudentStatistics(userId));
  }

  // Добавление решения задачи
  @PostMapping("/api/student/answer/{userId}/{homeworkId}/{taskId}")
  @PreAuthorize("@dataSecurityService.isOwner(#userId)")
  public ResponseEntity<StudentAnswers> submitAnswer(
          @PathVariable Long userId,
          @PathVariable Long homeworkId,
          @PathVariable Long taskId,
          @RequestBody StudentAnswersRequest answerRequest
  ) {
    return ResponseEntity.ok(studentService.submitAnswer(userId, homeworkId, taskId, answerRequest));
  }

}
