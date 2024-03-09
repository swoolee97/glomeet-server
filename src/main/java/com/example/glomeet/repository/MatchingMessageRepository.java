package com.example.glomeet.repository;

import com.example.glomeet.mongo.model.MatchingMessage;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchingMessageRepository extends MongoRepository<MatchingMessage, String> {
    List<MatchingMessage> findMatchingMessagesByMatchingRoomId(String matchingRoomId);

}
