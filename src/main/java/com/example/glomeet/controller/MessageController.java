package com.example.glomeet.controller;

import com.example.glomeet.mongo.model.MatchingMessage;
import com.example.glomeet.service.MatchingService;
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

    @MessageMapping("/chat/{matchingRoomId}")
    public void sendMessage(@Payload MatchingMessage matchingMessage, @DestinationVariable String matchingRoomId) {
        template.convertAndSend("/sub/chat/" + matchingRoomId, matchingMessage);
        matchingService.saveMessageToRedis(matchingMessage);
    }

    @MessageMapping("/meeting/{meetingId}")
    public void abc() {

    }

}
