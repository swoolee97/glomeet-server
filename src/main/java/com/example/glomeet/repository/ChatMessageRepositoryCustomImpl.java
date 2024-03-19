package com.example.glomeet.repository;

import com.example.glomeet.mongo.model.ChatMessage;
import java.time.Instant;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryCustomImpl implements ChatMessageRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Override
    public void updateUnreadCountAfterDate(String roomId, Date date) {
        Instant instant = date.toInstant();
        Query query = new Query(Criteria.where("roomId").is(roomId).and("sendAt").gt(Date.from(instant)));
        Update update = new Update().inc("readCount", 1);
        mongoTemplate.updateMulti(query, update, ChatMessage.class);
    }
}
