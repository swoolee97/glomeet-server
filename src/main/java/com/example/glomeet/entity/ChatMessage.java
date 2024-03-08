package com.example.glomeet.entity;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private int id;
    private String message;
    private String senderEmail;
    private String chatRoomId;
    private Timestamp sendAt;
}
