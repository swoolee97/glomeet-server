package com.example.glomeet.dto;

import com.example.glomeet.entity.Meeting;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingChatInfoDTO {
    private Meeting meeting;
    @NotNull
    private Date sendAt;
    @Nullable
    private String lastMessage;

}
