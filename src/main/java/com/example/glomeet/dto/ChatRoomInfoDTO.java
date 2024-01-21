package com.example.glomeet.dto;

import java.sql.Timestamp;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ChatRoomInfoDTO {
    private int id;
    private String message;
    private Timestamp sendAt;
}
