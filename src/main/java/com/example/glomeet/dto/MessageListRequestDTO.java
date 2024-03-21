package com.example.glomeet.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Date;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessageListRequestDTO {
    @NotNull
    private String roomId;
    @Nullable
    private Date lastReadAt;
}
