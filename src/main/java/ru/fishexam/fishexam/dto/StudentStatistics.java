package ru.fishexam.fishexam.dto;

public class StudentStatistics {
    private Long studentId;
    private String firstName;
    private String surname;
    private int totalTasks;
    private int correctTasks;
    private int incorrectTasks;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public int getCorrectTasks() {
        return correctTasks;
    }

    public void setCorrectTasks(int correctTasks) {
        this.correctTasks = correctTasks;
    }

    public int getIncorrectTasks() {
        return incorrectTasks;
    }

    public void setIncorrectTasks(int incorrectTasks) {
        this.incorrectTasks = incorrectTasks;
    }
}