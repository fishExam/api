package ru.fishexam.fishexam.dto.user;

import jakarta.annotation.Nullable;

import java.time.LocalDate;

public class UserProfile {
    private Long userId;
    private String surname;
    private String firstName;
    @Nullable
    private String patronymic;
    private String phone;
    @Nullable
    private String email;
    private LocalDate birth;
    private String telegramId;

    public UserProfile(Long userId, String surname, String firstName, @Nullable String patronymic, String phone, @Nullable String email, LocalDate birth, String telegramId) {
        this.userId = userId;
        this.surname = surname;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.phone = phone;
        this.email = email;
        this.birth = birth;
        this.telegramId = telegramId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
