package com.example.glomeet.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatMessage {
    private final String message;
    private final String senderEmail;
    private final String chatRoomId;
}
