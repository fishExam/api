package ru.fishexam.fishexam.dto;

public class TaskStatistic {
    private Long taskId;
    private String title;
    private int totalStudents;
    private int correctStudents;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    public int getCorrectStudents() {
        return correctStudents;
    }

    public void setCorrectStudents(int correctStudents) {
        this.correctStudents = correctStudents;
    }
}
