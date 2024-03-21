package com.example.glomeet.controller;

import com.example.glomeet.dto.ChatInfoDTO;
import com.example.glomeet.service.ChattingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/matching")
public class MatchingController {

    private final ChattingService chattingService;

    @PostMapping("/generate")
    public void generateChatRoom() {
//        chatService.createChatRoom();
    }

    @PostMapping("/list")
    public ResponseEntity<List<ChatInfoDTO>> extractMatchingList() {
        List<ChatInfoDTO> matchingRoomInfos = chattingService.findMatchingRoomInfoByEmail();
        return new ResponseEntity<>(matchingRoomInfos, HttpStatus.OK);
    }


}
