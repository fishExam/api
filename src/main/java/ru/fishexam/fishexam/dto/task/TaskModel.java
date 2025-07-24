package ru.fishexam.fishexam.dto.task;

public class TaskModel {
    private Long taskId;
    private Long authorId;
    private String title;
    private String answer;

    public TaskModel(Long taskId, Long authorId, String title, String answer) {
        this.taskId = taskId;
        this.authorId = authorId;
        this.title = title;
        this.answer = answer;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
