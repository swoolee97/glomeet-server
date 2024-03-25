package com.example.glomeet.controller;

import com.example.glomeet.dto.UserDTO;
import org.springframework.web.bind.annotation.*;
import com.example.glomeet.service.MatchService;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchController {
    private final MatchService matchingService;

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody UserDTO user){
        matchingService.addUser(user);

        String statusMessage = matchingService.getMatchingStatus();
        return ResponseEntity.ok(statusMessage);
    }

    @GetMapping("/find/{email}")
    public ResponseEntity<?> findMatch(@PathVariable String email) {
        for (UserDTO currentUser : matchingService.getUsers()){
            if (currentUser.getEmail().equals(email)){
                UserDTO match = matchingService.findMatch(currentUser);
                if (match != null){
                    String statusMessage = matchingService.getMatchingStatus();
                    return ResponseEntity.ok(statusMessage);
                } else {
                    return ResponseEntity.ok(matchingService.getMatchingStatus());
                }
            }
        } return ResponseEntity.badRequest().body("매칭 오류입니다.");
    }

    @PostMapping("/setTime")
    public ResponseEntity<String> setCurrentTime(@RequestBody Map<String, String> requestBody) {
        String currentTimeStr = requestBody.get("currentTime");
        LocalDateTime currentTime = LocalDateTime.parse(currentTimeStr);
        matchingService.setCurrentTimeForTesting(currentTime);
        return ResponseEntity.ok("현재 시간이 설정되었습니다: " + currentTimeStr);
    }




    @Getter
    public static class MatchingDTO{
        @NotNull
        private String hopeInterest;
        private String hopeType;
        @NotNull
        private String country;
    }
}
