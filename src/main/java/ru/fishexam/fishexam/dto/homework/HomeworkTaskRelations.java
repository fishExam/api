package ru.fishexam.fishexam.dto.homework;

public class HomeworkTaskRelations {
    private Long homeworkTaskId;
    private Long homeworkId;
    private Long taskId;

    public HomeworkTaskRelations(Long homeworkTaskId, Long homeworkId, Long taskId) {
        this.homeworkTaskId = homeworkTaskId;
        this.homeworkId = homeworkId;
        this.taskId = taskId;
    }

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
