package com.example.glomeet.repository;

import com.example.glomeet.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    Message findByMeetingId(String meetingId);
}
