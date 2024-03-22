package com.example.glomeet.repository;

import com.example.glomeet.mongo.model.ChatMessage;
import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    ChatMessage findTopByRoomId(String roomId);

    @Query(value = "{'roomId': ?0, 'sendAt': {'$gt': ?1}}", count = true)
    int countMessagesAfterLastLeftAt(String roomId, Date lastLeftAt);

    List<ChatMessage> findTop500ByRoomIdAndSendAtLessThanOrderBySendAtDesc(String roomId, Date lastReadAt);

    List<ChatMessage> findByRoomIdAndSendAtGreaterThanEqualOrderBySendAtDesc(String roomId, Date lastReadAt);


}
