package com.example.glomeet.mongo.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chatMessage")
public class ChatMessage {

    @Transient
    public static final String SEQUENCE_NAME = "CHAT_MESSAGE_SEQUENCE";

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
