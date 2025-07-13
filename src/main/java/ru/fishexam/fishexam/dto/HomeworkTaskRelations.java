package ru.fishexam.fishexam.dto;

public class HomeworkTaskRelations {
    private Long homeworkTaskId;
    private Long homeworkId;
    private Long taskId;

    public Long getHomeworkTaskId() {
        return homeworkTaskId;
    }

    public void setHomeworkTaskId(Long homeworkTaskId) {
        this.homeworkTaskId = homeworkTaskId;
    }

    public Long getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(Long homeworkId) {
        this.homeworkId = homeworkId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
