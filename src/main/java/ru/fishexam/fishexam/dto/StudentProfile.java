package ru.fishexam.fishexam.dto;

import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.Date;

public class StudentProfile extends UserProfile {
    private Long studentId;
    private String surname;
    private String firstName;
    @Nullable
    private String patronymic;
    private String phone;
    private String email;
    private LocalDate birth;
    private String telegramId;
    @Nullable
    private String parentId;
    @Nullable
    private Integer tasksCount;

    public StudentProfile(Long studentId, String surname, String firstName, @Nullable String patronymic, String phone, String email, LocalDate birth, String telegramId, @Nullable String parentId, Integer tasksCount) {
        this.studentId = studentId;
        this.surname = surname;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.phone = phone;
        this.email = email;
        this.birth = birth;
        this.telegramId = telegramId;
        this.parentId = parentId;
        this.tasksCount = tasksCount != null ? tasksCount : 0;
    }

    public StudentProfile() {
        this.surname = super.getUsername();
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String first_name) {
        this.firstName = first_name;
    }

    @Nullable
    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(@Nullable String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public String getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(String telegram_id) {
        this.telegramId = telegram_id;
    }

    @Nullable
    public String getParentId() {
        return parentId;
    }

    public void setParentId(@Nullable String parent_id) {
        this.parentId = parent_id;
    }

    public int getTasksCount() {
        return tasksCount;
    }

    public void setTasksCount(int tasks_count) {
        this.tasksCount = tasks_count;
    }
}
