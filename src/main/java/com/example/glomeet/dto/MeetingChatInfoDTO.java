package com.example.glomeet.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingChatInfoDTO {
    private String id;
    private String title;
    @NotNull
    private Date sendAt;
    @Nullable
    private String lastMessage;

}
