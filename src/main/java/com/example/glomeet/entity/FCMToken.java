package com.example.glomeet.entity;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FCMToken {

    private int TokenId;
    private String userEmail;
    private String token;
    private String deviceInfo;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
