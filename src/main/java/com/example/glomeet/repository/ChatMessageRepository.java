package com.example.glomeet.repository;

import com.example.glomeet.mongo.model.ChatMessage;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findMatchingMessagesByRoomId(String roomId);

}
