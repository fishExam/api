package ru.fishexam.fishexam.auth.models;

public class UserAuth {
    private Long userid;
    private String username;
    private String password;

    public UserAuth(Long userid, String username, String password) {
        this.userid = userid;
        this.username = username;
        this.password = password;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
