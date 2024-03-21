package com.example.glomeet.repository;

import com.example.glomeet.dto.ChatInfoDTO;
import com.example.glomeet.mongo.model.ChatMessage;
import com.example.glomeet.mongo.model.LastReadTime;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        Query query = new Query(Criteria.where("roomId").is(roomId).and("sendAt").gt(date));
        Update update = new Update().inc("readCount", 1);
        mongoTemplate.updateMulti(query, update, ChatMessage.class);
    }

    @Override
    public Map<String, Date> getLastReadMap(String email, List<ChatInfoDTO> chatInfos) {
        Map<String, Date> lastReadMap = new HashMap<>();
        chatInfos.forEach(info -> {
            String roomId = info.getId();
            Query query = new Query(Criteria.where("email").is(email).and("roomId").is(roomId));
            LastReadTime lastReadTime = mongoTemplate.findOne(query, LastReadTime.class);
            if (lastReadTime == null) {
                lastReadMap.put(roomId, Date.from(Instant.now()));
            } else {
                lastReadMap.put(roomId, lastReadTime.getAt());
            }
        });
        return lastReadMap;
    }

}
