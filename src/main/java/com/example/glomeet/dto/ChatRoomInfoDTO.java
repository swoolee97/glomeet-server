package com.example.glomeet.dto;

import java.sql.Timestamp;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ChatRoomInfoDTO {
    private String id;
    private String message;
    private Timestamp sendAt;
    private String partnerEmail;
}
