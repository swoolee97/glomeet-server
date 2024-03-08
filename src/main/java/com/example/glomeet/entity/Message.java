package com.example.glomeet.entity;

import java.sql.Timestamp;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Builder
@Document(collection = "message")
public class Message {
    @Id
    private int id;
    @Field
    private String message;
    @Field
    private String meetingId;
    @Field
    private String senderEmail;
    @Field
    private Timestamp sendAt;
}
