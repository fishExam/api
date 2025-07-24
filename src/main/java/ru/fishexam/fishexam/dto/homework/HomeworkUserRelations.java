package ru.fishexam.fishexam.dto.homework;

public class HomeworkUserRelations {
    private Long homeworkUserId;
    private Long homeworkId;
    private Long studentId;

    public Long getHomeworkUserId() {
        return homeworkUserId;
    }

    public void setHomeworkUserId(Long homeworkUserId) {
        this.homeworkUserId = homeworkUserId;
    }

    public Long getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(Long homeworkId) {
        this.homeworkId = homeworkId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
