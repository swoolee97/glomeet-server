package com.example.glomeet.service;

import com.example.glomeet.mongo.model.MatchingMessage;
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

    public void saveMessageToRedis(MatchingMessage matchingMessage) {
        Instant instant = Instant.now().atZone(ZoneId.of("Asia/Seoul")).toInstant();
        Timestamp timestamp = Timestamp.from(instant);
        matchingMessage.setSendAt(timestamp);
        addMessageToRedis(matchingMessage);
        saveLastMessage(matchingMessage);
    }

    public void saveLastMessage(MatchingMessage matchingMessage) {
        valueOperations = redisTemplate.opsForValue();
        valueOperations.set(LAST_MESSAGE_PREFIX + matchingMessage.getMatchingRoomId(), matchingMessage);
    }

    public void addMessageToRedis(MatchingMessage matchingMessage) {
        redisTemplate.opsForList()
                .rightPush(MESSAGE_LIST_PREFIX + matchingMessage.getMatchingRoomId(), matchingMessage);
    }
}
