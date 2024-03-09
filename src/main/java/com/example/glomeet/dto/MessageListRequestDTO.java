package com.example.glomeet.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MessageListRequestDTO {
    @NotNull
    private final String matchingRoomId;
    @Nullable
    private final Optional<Integer> lastMessageId;
}
