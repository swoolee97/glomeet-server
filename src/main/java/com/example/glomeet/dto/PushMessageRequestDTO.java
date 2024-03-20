package com.example.glomeet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PushMessageRequestDTO {
    private String email;
    private String title;
    private String body;
}
