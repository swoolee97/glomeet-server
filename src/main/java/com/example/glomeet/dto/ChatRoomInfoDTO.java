package com.example.glomeet.dto;

import java.util.Date;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ChatRoomInfoDTO {
    private String id;
    private String message;
    private Date sendAt;
    private String partnerEmail;
}
