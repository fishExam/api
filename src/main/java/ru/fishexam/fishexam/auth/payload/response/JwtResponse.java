package ru.fishexam.fishexam.auth.payload.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Builder
@AllArgsConstructor
@Getter
@Setter
@Data
public class JwtResponse {
  private String token;
  private String username;
  private Long userId;
  private String refreshToken;
}
