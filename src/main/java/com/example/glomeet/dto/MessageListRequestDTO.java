package com.example.glomeet.dto;

import jakarta.validation.constraints.NotNull;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MessageListRequestDTO {
    @NotNull
    private final String roomId;
    @Nullable
    private final String lastReadAt;
}
