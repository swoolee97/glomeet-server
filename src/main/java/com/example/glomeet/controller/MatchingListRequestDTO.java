package com.example.glomeet.controller;

import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MatchingListRequestDTO {
    private Map<String, String> lastLeftMap;
}
