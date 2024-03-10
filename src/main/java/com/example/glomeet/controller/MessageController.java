package com.example.glomeet.controller;

import com.example.glomeet.mongo.model.ChatMessage;
import com.example.glomeet.service.ChattingService;
import java.time.Instant;
import java.util.Date;
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
    private final ChattingService chattingService;
    private static final String CHAT_MESSAGE_PREFIX = "chat:";

    @MessageMapping("/chat/{roomId}")
    public void sendMessage(@Payload ChatMessage message, @DestinationVariable String roomId) {
        message.setSendAt(Date.from(Instant.now()));
        template.convertAndSend("/sub/chat/" + roomId, message);
        chattingService.saveMessageToRedis(message);
    }

    @MessageMapping("/meeting/{meetingId}")
    public void abc() {

    }

}
