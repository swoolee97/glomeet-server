package com.example.glomeet.controller;

import com.example.glomeet.mongo.model.ChatMessage;
import com.example.glomeet.mongo.model.ChatMessage.Type;
import com.example.glomeet.service.ChattingService;
import com.example.glomeet.service.MessageService;
import java.time.Instant;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final ChattingService chattingService;

    @MessageMapping("/chat/{roomId}")
    public void sendMessage(@Payload ChatMessage message, @DestinationVariable String roomId) {
        Type type = message.getType();
        message.setSendAt(Date.from(Instant.now()));
        if (type.equals(Type.JOIN) || type.equals(Type.LEAVE)) {
            chattingService.addMessageToRedis(message);
        } else if (type.equals(Type.SEND)) {
            message = messageService.updateAndGetActiveUserCount(message);
            chattingService.saveMessageToRedis(message);
            messageService.sendMessage(roomId, message);
        } else if (type.equals(Type.ENTER) || type.equals(Type.EXIT)) {
            message = messageService.updateAndGetActiveUserCount(message);
        }
    }

}
