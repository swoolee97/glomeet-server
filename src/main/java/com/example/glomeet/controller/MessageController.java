package com.example.glomeet.controller;

import com.example.glomeet.mongo.model.MatchingMessage;
import com.example.glomeet.service.MatchingService;
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
    private final MatchingService matchingService;
    private static final String CHAT_MESSAGE_PREFIX = "chat:";

    @MessageMapping("/chat/{roomId}")
    public void sendMessage(@Payload MatchingMessage message, @DestinationVariable String roomId) {
        message.setSendAt(Date.from(Instant.now()));
        template.convertAndSend("/sub/chat/" + roomId, message);
        matchingService.saveMessageToRedis(message);
    }

    @MessageMapping("/meeting/{meetingId}")
    public void abc() {

    }

}
