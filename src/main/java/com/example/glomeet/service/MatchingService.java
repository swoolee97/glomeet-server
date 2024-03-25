package com.example.glomeet.service;
import com.example.glomeet.controller.MatchingController;
import com.example.glomeet.dto.UserDTO;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class MatchingService {
    private final List<UserDTO> users = new ArrayList<>();
    private final LocalDateTime startTime = LocalDateTime.now();
    private LocalDateTime currentTime = LocalDateTime.now();



    public List<UserDTO> getUsers() {
        return users;
    }
    public String getMatchingStatus() {
        long minutesElapsed = ChronoUnit.MINUTES.between(startTime, getCurrentTime());
        if (minutesElapsed < 5){
            return "매칭중입니다.";
        } else if (minutesElapsed < 30){
            return "더 넓은 범위에서 매칭 중입니다.";
        } else {
            return "매칭 실패";
        }
    }
    public void addUser(UserDTO user) {
        this.users.add(user);
    }

    public UserDTO findMatch(UserDTO currentUser){
        List<String> currentUserInterests = Arrays.asList(currentUser.getInterest().split(","));


        return users.stream()
                .filter(user -> !user.getEmail().equals(currentUser.getEmail()))
                .filter(user -> isMatch(currentUser, user, currentUserInterests))
                .findFirst()
                .orElse(null);

    }

    private boolean isMatch(UserDTO currentUser, UserDTO user, List<String> currentUserInterests) {
        List<String> userInterests = Arrays.asList(user.getInterest().split(","));

        long minutesElapsed = ChronoUnit.MINUTES.between(startTime, getCurrentTime());
        boolean isFlexible = minutesElapsed > 5;

        if (isFlexible){

            return userInterests.stream().anyMatch(currentUserInterests::contains);
        }else {
            return currentUserInterests.containsAll(userInterests) && userInterests.containsAll(currentUserInterests) && currentUser.getType().equals(user.getType());
        }
    }



    // 테스트를 위해 현재 시간을 설정하는 메소드
    public void setCurrentTimeForTesting(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }

    // 현재 시간 반환 메소드 수정
    private LocalDateTime getCurrentTime() {
        return currentTime;
    }
}
