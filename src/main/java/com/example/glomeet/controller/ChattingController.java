package com.example.glomeet.controller;

import com.example.glomeet.service.ChattingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/chat")
@RestController
@RequiredArgsConstructor
public class ChattingController {

    private final ChattingService chattingService;

    @PostMapping("/my")
    public ResponseEntity<?> getMyChatList() {
        List<String> chatIdList = chattingService.getMyChatList();
        return ResponseEntity.ok(chatIdList);
    }
}
