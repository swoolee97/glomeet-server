package com.example.glomeet.dto;

import lombok.Getter;

@Getter
public class PushMessageRequestDTO {
    private String email;
    private String title;
    private String body;
}
