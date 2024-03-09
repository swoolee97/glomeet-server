package com.example.glomeet.controller;

import com.example.glomeet.mongo.model.ChatMessage;
import com.example.glomeet.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달
    private final ChatService chatService;
    private static final String CHAT_MESSAGE_PREFIX = "chat:";

    @MessageMapping("/chat/{chatRoomId}")
    public void sendMessage(@Payload ChatMessage chatMessage, @DestinationVariable String chatRoomId) {
        template.convertAndSend("/sub/chat/" + chatRoomId, chatMessage);
        chatService.saveMessageToRedis(chatMessage);
    }

    @MessageMapping("/meeting/{meetingId}")
    public void abc() {

    }

}
