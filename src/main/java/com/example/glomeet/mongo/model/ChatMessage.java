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
    private String roomId;
    @Field
    private String senderEmail;
    @Field
    private String senderNickName;
    @Field
    private Date sendAt;
    @Field
    private Type type;
    @Field
    private int readCount;

    /*
    CREATE : 방 생성
    JOIN : 방 참가
    LEAVE : 빙 퇴장
    SEND : 메세지 보내기
    ENTER : 방 입장
    EXIT : 방 나가기
    JOIN, LEAVE는 미팅에 처음 참여하거나 아예 나갈 때 쓰는 type.
    ENTER, EXIT은 채팅하려고 들어가거나, 채팅 끝내고 나갈 때 쓰는 type
     */

    public enum Type {
        CREATE, JOIN, LEAVE, SEND, ENTER, EXIT;
    }

}
