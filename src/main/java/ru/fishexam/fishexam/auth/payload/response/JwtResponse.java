package ru.fishexam.fishexam.auth.payload.response;


public class JwtResponse {
  private String token;
  private String username;
  private Long userId;
  private String refreshToken;

  public JwtResponse(String token, String username, Long userId, String refreshToken) {
    this.token = token;
    this.username = username;
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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
