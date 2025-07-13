package ru.fishexam.fishexam.dto;

import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.Date;

public class TeacherProfile extends UserProfile {
    private Long teacherId;
    private String surname;
    private String firstName;
    @Nullable
    private String patronymic;
    private String phone;
    private String email;
    private LocalDate birth;
    private String telegramId;
    public TeacherProfile() {
        this.surname = super.getUsername();
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
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

    public void setTelegramId(String telegramId) {
        this.telegramId = telegramId;
    }
}
