package com.example.glomeet.controller;

import com.example.glomeet.dto.ChatRoomInfoDTO;
import com.example.glomeet.service.ChatService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/generate")
    public void generateChatRoom() {
//        chatService.createChatRoom();
    }

    @PostMapping("/list")
    public ResponseEntity<List<ChatRoomInfoDTO>> extractChatList(@RequestBody Map<String, String> requestBody) {
        List<ChatRoomInfoDTO> chatRoomInfos = chatService.findChatRoomInfoByEmail(requestBody.get("email"));
        return new ResponseEntity<>(chatRoomInfos, HttpStatus.OK);
    }

}
