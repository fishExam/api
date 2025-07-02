package ru.fishexam.fishexam.auth.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Builder
@Getter
public class UserAuth {
    private Long userid;
    private String username;
    private String password;
}
