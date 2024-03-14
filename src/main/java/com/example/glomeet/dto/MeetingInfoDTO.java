package com.example.glomeet.dto;

import com.example.glomeet.entity.Meeting;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingInfoDTO {
    private Meeting meeting;
    private int participants;
}
