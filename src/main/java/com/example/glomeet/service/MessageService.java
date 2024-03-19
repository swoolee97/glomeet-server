package com.example.glomeet.service;

import com.example.glomeet.dto.MessageListRequestDTO;
import com.example.glomeet.mongo.model.ChatMessage;
import com.example.glomeet.mongo.model.ChatMessage.Type;
import com.example.glomeet.repository.ChatMessageRepositoryCustomImpl;
import com.example.glomeet.util.DateUtil;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final RedisTemplate<String, Integer> redisTemplate;
    private ValueOperations<String, Integer> valueOperations;
    private final ChatMessageRepositoryCustomImpl chatMessageRepositoryCustom;

    private static final String COUNT_ACTIVE_USER_PREFIX = "activeUsers:";

    public ChatMessage updateAndGetActiveUserCount(ChatMessage message) {
        Type type = message.getType();
        String key = COUNT_ACTIVE_USER_PREFIX + message.getRoomId();
        valueOperations = redisTemplate.opsForValue();
        if (type.equals(Type.ENTER)) {
            valueOperations.setIfAbsent(key, 0);
            valueOperations.increment(key);
        } else if (type.equals(Type.EXIT)) {
            valueOperations.setIfAbsent(key, 1);
            valueOperations.decrement(key);
        }
        message.setReadCount(valueOperations.get(key));
        return message;
    }

    public void updateUnReadUserCount(MessageListRequestDTO messageListRequestDTO) {
        String id = messageListRequestDTO.getRoomId();
        Date lastReadAt = DateUtil.parseDate(messageListRequestDTO.getLastReadAt());
        chatMessageRepositoryCustom.updateUnreadCountAfterDate(id, lastReadAt);
    }

}
