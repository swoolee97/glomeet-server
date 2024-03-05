package com.example.glomeet.entity;

import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Meeting {
    private String id;
    @NotNull
    private String title;
    @NotNull
    private String comment;
    @NotNull
    private int capacity;
    @NotNull
    private String category;
    @NotNull
    private String location;
    private Timestamp meetingDate;
    private String url;
    private String status;
    private String masterEmail;
}
