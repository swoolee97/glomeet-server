package com.example.glomeet.service;

import com.example.glomeet.dto.MatchingRoomInfoDTO;
import com.example.glomeet.dto.MessageListRequestDTO;
import com.example.glomeet.mapper.MatchingMapper;
import com.example.glomeet.mongo.model.MatchingMessage;
import com.example.glomeet.repository.MatchingMessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private static final String LAST_MESSAGE_PREFIX = "lastMessage:";
    private static final String MESSAGE_LIST_PREFIX = "matchingRoom:";
    private final MatchingMapper matchingMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> valueOperations;
    private ListOperations<String, Object> listOperations;
    private final ObjectMapper objectMapper;
    private final MongoTemplate mongoTemplate;
    private final MatchingMessageRepository matchingMessageRepository;

    public void createChatRoom(int chatRoomId, List<String> emails) {
        matchingMapper.insertMatchingRoom();
        emails.forEach(email -> matchingMapper.insertMatchingUser(chatRoomId, email));
    }

    public void deleteChatRoom() {
        matchingMapper.deleteMatchingRoom();
    }

    public List<String> findChatRoomByEmail(String email) {
        return matchingMapper.findMatchingRoomByEmail(email);
    }

    public List<MatchingRoomInfoDTO> findLastMessageByMatchingRoomId(List<MatchingRoomInfoDTO> list) {
        valueOperations = redisTemplate.opsForValue();
        list.forEach(matchingRoomInfoDTO -> {
            MatchingMessage message = (MatchingMessage) valueOperations.get(
                    LAST_MESSAGE_PREFIX + matchingRoomInfoDTO.getId());
            
            if (!Objects.isNull(message)) {
                matchingRoomInfoDTO.setMessage(message.getMessage());
                matchingRoomInfoDTO.setSendAt(message.getSendAt());
            }
        });
        return list;
    }

    public List<MatchingRoomInfoDTO> findMatchingRoomInfoByEmail(String email) {
        return matchingMapper.findMatchingRoomInfoByEmail(email);
    }

    public List<MatchingMessage> MatchingMessageByChatRoomId(MessageListRequestDTO messageListRequestDTO) {
        commitMessagesToDatabase(messageListRequestDTO);
        return matchingMessageRepository.findMatchingMessagesByMatchingRoomId(
                messageListRequestDTO.getMatchingRoomId());
    }

    public void commitMessagesToDatabase(MessageListRequestDTO messageListRequestDTO) {
        listOperations = redisTemplate.opsForList();
        List<Object> list = listOperations.range(MESSAGE_LIST_PREFIX + messageListRequestDTO.getMatchingRoomId(), 0,
                -1);
        if (!list.isEmpty()) {
            List<MatchingMessage> messages = list.stream()
                    .map(json -> objectMapper.convertValue(json, MatchingMessage.class))
                    .collect(Collectors.toList());
            mongoTemplate.insertAll(messages);
            redisTemplate.delete(MESSAGE_LIST_PREFIX + messageListRequestDTO.getMatchingRoomId());
        }
    }

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
