package com.example.glomeet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenDTO {
    private String accessToken;
    private String refreshToken;
}
