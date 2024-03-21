package com.example.glomeet.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateLastReadTimeDTO {
    @NotNull
    private String roomId;
    @NotNull
    private String email;
    @Nullable
    private Date date;
}
