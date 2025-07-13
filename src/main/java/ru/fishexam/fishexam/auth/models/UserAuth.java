package ru.fishexam.fishexam.auth.models;

import java.time.LocalDate;
import java.util.Date;

public class UserAuth {
    private Long userid;
    private String surname;
    private String first_name;
    private String patronymic;
    private String phone;
    private String email;
    private LocalDate birth;
    private String telegram_id;
    private String password;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public String getTelegram_id() {
        return telegram_id;
    }

    public void setTelegram_id(String telegram_id) {
        this.telegram_id = telegram_id;
    }

    public UserAuth(Long userid, String username, String first_name, String patronymic, String phone, String email, LocalDate birth, String telegram_id, String password) {
        this.userid = userid;
        this.surname = username;
        this.first_name = first_name;
        this.patronymic = patronymic;
        this.phone = phone;
        this.email = email;
        this.birth = birth;
        this.telegram_id = telegram_id;
        this.password = password;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public void setSurname(String username) {
        this.surname = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUserid() {
        return userid;
    }

    public String getSurname() {
        return surname;
    }

    public String getPassword() {
        return password;
    }
}
