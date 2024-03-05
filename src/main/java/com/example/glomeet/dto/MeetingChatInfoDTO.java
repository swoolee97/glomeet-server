package com.example.glomeet.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingChatInfoDTO {
    @NotNull
    private int meetingId;
    @NotNull
    private String title;
    @NotNull
    private Timestamp sendAt;
    @Nullable
    private String lastMessage;

}
