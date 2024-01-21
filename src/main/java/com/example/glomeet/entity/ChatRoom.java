package com.example.glomeet.entity;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoom {
    private int id;
    private Timestamp createdAt;
    private String roomStatus;
}
