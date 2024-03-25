package com.example.glomeet.entity;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder

public class Matching {
    private String id;
    @NotNull
    private String category;
    @NotNull
    private String location;
    private Timestamp matchingDate;
}
