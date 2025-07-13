package ru.fishexam.fishexam.dto;

public class StudentAnswers {
    private Long studentAnswersId;
    private Long homeworkUserId;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    private Long taskId;
    private String studentAnswer;
    private boolean isCorrect;
    private String feedback;

    public Long getStudentAnswersId() {
        return studentAnswersId;
    }

    public void setStudentAnswersId(Long studentAnswersId) {
        this.studentAnswersId = studentAnswersId;
    }

    public Long getHomeworkUserId() {
        return homeworkUserId;
    }

    public void setHomeworkUserId(Long homeworkUserId) {
        this.homeworkUserId = homeworkUserId;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public boolean getIsCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}