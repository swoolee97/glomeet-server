package com.example.glomeet.service;

import com.example.glomeet.mongo.model.ChatMessage;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChattingService {
    private static final String LAST_MESSAGE_PREFIX = "lastMessage:";
    private static final String MESSAGE_LIST_PREFIX = "chat:";
    private final RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> valueOperations;

    public void saveMessageToRedis(ChatMessage chatMessage) {
        Instant instant = Instant.now().atZone(ZoneId.of("Asia/Seoul")).toInstant();
        Timestamp timestamp = Timestamp.from(instant);
        chatMessage.setSendAt(timestamp);
        addMessageToRedis(chatMessage);
        saveLastMessage(chatMessage);
    }

    public void saveLastMessage(ChatMessage chatMessage) {
        valueOperations = redisTemplate.opsForValue();
        valueOperations.set(LAST_MESSAGE_PREFIX + chatMessage.getRoomId(), chatMessage);
    }

    public void addMessageToRedis(ChatMessage chatMessage) {
        redisTemplate.opsForList()
                .rightPush(MESSAGE_LIST_PREFIX + chatMessage.getRoomId(), chatMessage);
    }
}
