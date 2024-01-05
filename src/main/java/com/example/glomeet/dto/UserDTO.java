package com.example.glomeet.dto;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    @NonNull
    private int id;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private Timestamp signUpDate;
    @NonNull
    private String nickName;
    @NonNull
    private String role;
}
