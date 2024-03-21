package com.example.glomeet.repository;

import com.example.glomeet.dto.MessageListRequestDTO;
import com.example.glomeet.dto.UpdateLastReadTimeDTO;
import com.example.glomeet.mongo.model.LastReadTime;
import com.example.glomeet.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
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
public class LastReadTimeRepository {
    private final MongoTemplate mongoTemplate;
    private final UserDetailsServiceImpl userDetailsService;

    public void updateLastReadTime(@Valid UpdateLastReadTimeDTO updateLastReadTimeDTO) {
        Query query = Query.query(Criteria.where("roomId").is(updateLastReadTimeDTO.getRoomId()).and("email")
                .is(updateLastReadTimeDTO.getEmail()));
        Update update = Update.update("at", updateLastReadTimeDTO.getDate());
        mongoTemplate.upsert(query, update, LastReadTime.class);
    }

    public Date findLastReadTimeByRoomIdAndEmail(MessageListRequestDTO messageListRequestDTO) {
        String email = userDetailsService.getUserNameByAccessToken();
        Query query = Query.query(
                Criteria.where("roomId").is(messageListRequestDTO.getRoomId()).and("email").is(email));
        LastReadTime lastReadTime = mongoTemplate.findOne(query, LastReadTime.class);
        if (lastReadTime == null) {
            return Date.from(Instant.now());
        }
        return lastReadTime.getAt();
    }

}
