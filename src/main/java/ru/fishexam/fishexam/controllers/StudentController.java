package ru.fishexam.fishexam.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.fishexam.fishexam.dto.StudentProfile;
import ru.fishexam.fishexam.dto.StudentProfileRequest;
import ru.fishexam.fishexam.dto.UserProfile;
import ru.fishexam.fishexam.service.StudentService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class StudentController {
  private final StudentService studentService;

  public StudentController(StudentService studentService) {
      this.studentService = studentService;
  }

  @GetMapping("/api/profile/{userId}")
  @PreAuthorize("@dataSecurityService.isOwner(#userId)")
  public ResponseEntity<UserProfile> userAccess(
          @PathVariable Long userId
  ) {
    return ResponseEntity.ok(studentService.getById(userId));
  }

  @PostMapping("/api/profile/{userId}")
  @PreAuthorize("@dataSecurityService.isOwner(#userId)")
  public ResponseEntity<StudentProfile> updateProfile(
          @PathVariable Long userId,
          @RequestBody StudentProfileRequest studentProfileRequest
  ) {
    return ResponseEntity.ok(studentService.updateStudentProfile(userId, studentProfileRequest));
  }

}
