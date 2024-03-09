package com.example.glomeet.repository;

import com.example.glomeet.mongo.model.ChatMessage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findChatMessagesByChatRoomId(String chatRoomId);

    Optional<ChatMessage> findTopByChatRoomIdOrderBySendAtDesc(String chatRoomId);

}
