package com.example.glomeet.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatInfoDTO {
    private String id;
    private String title;
    private String lastMessage;
    private Date sendAt;
    private int unRead;
}
