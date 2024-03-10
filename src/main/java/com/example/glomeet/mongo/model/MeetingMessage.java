package com.example.glomeet.mongo.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "chatMessage")
public class MeetingMessage {

    @Id
    private String _id;
    @Field
    private String message;
    @Field
    private String chatRoomId;
    @Field
    private String senderEmail;
    @Field
    private Date sendAt;

}
