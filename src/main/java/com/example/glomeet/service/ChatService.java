package com.example.glomeet.service;

import com.example.glomeet.dto.ChatRoomInfoDTO;
import com.example.glomeet.dto.MessageListRequestDTO;
import com.example.glomeet.mapper.ChatMapper;
import com.example.glomeet.mongo.model.ChatMessage;
import com.example.glomeet.repository.ChatMessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private static final String LAST_MESSAGE_PREFIX = "lastMessage:";
    private static final String MESSAGE_LIST_PREFIX = "chatRoom:";
    private final ChatMapper chatMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> valueOperations;
    private ListOperations<String, Object> listOperations;
    private final ObjectMapper objectMapper;
    private final MongoTemplate mongoTemplate;
    private final ChatMessageRepository chatMessageRepository;

    public void createChatRoom(int chatRoomId, List<String> emails) {
        chatMapper.insertChatRoom();
        emails.forEach(email -> chatMapper.insertChatUser(chatRoomId, email));
    }

    public void deleteChatRoom() {
        chatMapper.deleteChatRoom();
    }

    public List<String> findChatRoomByEmail(String email) {
        return chatMapper.findChatRoomByEmail(email);
    }

    public List<ChatRoomInfoDTO> findLastMessageByChatRoomId(List<ChatRoomInfoDTO> list) {
        list.forEach(chatRoomInfoDTO -> {
            Optional<ChatMessage> message = chatMessageRepository.findTopByChatRoomIdOrderBySendAtDesc(
                    chatRoomInfoDTO.getId());
            if (message.isPresent()) {
                chatRoomInfoDTO.setMessage(message.get().getMessage());
                chatRoomInfoDTO.setSendAt(message.get().getSendAt());
            }
        });
        return list;
    }

    public List<ChatRoomInfoDTO> findChatRoomInfoByEmail(String email) {
        return chatMapper.findChatRoomInfoByEmail(email);
    }

    public List<ChatMessage> findChatMessageByChatRoomId(MessageListRequestDTO messageListRequestDTO) {
        commitMessagesToDatabase(messageListRequestDTO);
        return chatMessageRepository.findChatMessagesByChatRoomId(messageListRequestDTO.getChatRoomId());
    }

    public void commitMessagesToDatabase(MessageListRequestDTO messageListRequestDTO) {
        listOperations = redisTemplate.opsForList();
        List<Object> list = listOperations.range(MESSAGE_LIST_PREFIX + messageListRequestDTO.getChatRoomId(), 0, -1);
        if (!list.isEmpty()) {
            List<ChatMessage> messages = list.stream().map(json -> objectMapper.convertValue(json, ChatMessage.class))
                    .collect(Collectors.toList());
            mongoTemplate.insertAll(messages);
            redisTemplate.delete(MESSAGE_LIST_PREFIX + messageListRequestDTO.getChatRoomId());
        }
    }

    public void saveMessageToRedis(ChatMessage chatMessage) {
        Instant instant = Instant.now().atZone(ZoneId.of("Asia/Seoul")).toInstant();
        Timestamp timestamp = Timestamp.from(instant);
        chatMessage.setSendAt(timestamp);
        addMessageToRedis(chatMessage);
        saveLastMessage(chatMessage);
    }

    public void saveLastMessage(ChatMessage chatMessage) {
        valueOperations = redisTemplate.opsForValue();
        valueOperations.set(LAST_MESSAGE_PREFIX + chatMessage.getChatRoomId(), chatMessage);
    }

    public void addMessageToRedis(ChatMessage chatMessage) {
        redisTemplate.opsForList().rightPush(MESSAGE_LIST_PREFIX + chatMessage.getChatRoomId(), chatMessage);
    }

}
