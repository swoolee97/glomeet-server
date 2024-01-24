package com.example.glomeet.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessageListRequestDTO {
    @NotNull
    private final String chatRoomId;
    @Nullable
    private final Optional<Integer> lastMessageId;
}
