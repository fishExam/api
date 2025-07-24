package ru.fishexam.fishexam.auth.payload.response;


public class JwtResponse {
  private String token;
  private String surname;
  private Long userId;
  private String refreshToken;

  public JwtResponse(String token, String username, Long userId, String refreshToken) {
    this.token = token;
    this.surname = username;
    this.userId = userId;
    this.refreshToken = refreshToken;
  }

  public String getToken() {
    return this.token;
  }


  public Long getUserId() {
    return this.userId;
  }

  public String getRefreshToken() {
    return this.refreshToken;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
