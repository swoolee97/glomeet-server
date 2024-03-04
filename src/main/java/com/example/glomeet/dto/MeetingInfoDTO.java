package com.example.glomeet.dto;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingInfoDTO {
    private String id;
    private int capacity;
    private String category;
    private String location;
    private Timestamp meetingDate;
    private String url;
    private int participants;
}
