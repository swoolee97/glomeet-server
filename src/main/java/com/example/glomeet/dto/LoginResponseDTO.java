package com.example.glomeet.dto;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponseDTO {
    private Map<String, String> tokens;
    private String nickName;
}
