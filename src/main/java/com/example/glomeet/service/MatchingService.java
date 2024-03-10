package com.example.glomeet.service;

import com.example.glomeet.dto.MatchingRoomInfoDTO;
import com.example.glomeet.dto.MessageListRequestDTO;
import com.example.glomeet.mapper.MatchingMapper;
import com.example.glomeet.mongo.model.ChatMessage;
import com.example.glomeet.repository.ChatMessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private static final String MESSAGE_LIST_PREFIX = "chat:";
    private final MatchingMapper matchingMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> valueOperations;
    private ListOperations<String, Object> listOperations;
    private final ObjectMapper objectMapper;
    private final MongoTemplate mongoTemplate;
    private final ChatMessageRepository chatMessageRepository;

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
            ChatMessage message = (ChatMessage) valueOperations.get(
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

    public List<ChatMessage> MatchingMessageByChatRoomId(MessageListRequestDTO messageListRequestDTO) {
        commitMessagesToDatabase(messageListRequestDTO);
        return chatMessageRepository.findMatchingMessagesByRoomId(
                messageListRequestDTO.getRoomId());
    }

    public void commitMessagesToDatabase(MessageListRequestDTO messageListRequestDTO) {
        listOperations = redisTemplate.opsForList();
        List<Object> list = listOperations.range(MESSAGE_LIST_PREFIX + messageListRequestDTO.getRoomId(), 0,
                -1);
        if (!list.isEmpty()) {
            List<ChatMessage> messages = list.stream()
                    .map(json -> objectMapper.convertValue(json, ChatMessage.class))
                    .collect(Collectors.toList());
            mongoTemplate.insertAll(messages);
            redisTemplate.delete(MESSAGE_LIST_PREFIX + messageListRequestDTO.getRoomId());
        }
    }

}
