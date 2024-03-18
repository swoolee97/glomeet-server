package com.example.glomeet.service;

import com.example.glomeet.mongo.model.ChatMessage;
import com.example.glomeet.mongo.model.ChatMessage.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final RedisTemplate<String, Integer> redisTemplate;
    private ValueOperations<String, Integer> valueOperations;

    private static final String COUNT_ACTIVE_USER_PREFIX = "activeUsers:";

    public ChatMessage updateAndGetActiveUserCount(ChatMessage message) {
        Type type = message.getType();
        String key = COUNT_ACTIVE_USER_PREFIX + message.getRoomId();
        if (type.equals(Type.ENTER)) {
            valueOperations = redisTemplate.opsForValue();
            valueOperations.setIfAbsent(key, 0);
            valueOperations.increment(key);
        } else if (type.equals(Type.EXIT)) {
            valueOperations = redisTemplate.opsForValue();
            valueOperations.setIfAbsent(key, 1);
            valueOperations.decrement(key);
        }
        message.setReadCount(valueOperations.get(key));
        return message;
    }

}
