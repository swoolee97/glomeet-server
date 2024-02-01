package com.example.glomeet.controller;

import com.example.glomeet.dto.ChatRoomInfoDTO;
import com.example.glomeet.dto.MessageListRequestDTO;
import com.example.glomeet.entity.ChatMessage;
import com.example.glomeet.service.ChatService;
import com.example.glomeet.service.UserDetailsServiceImpl;
import java.util.List;
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
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @PostMapping("/generate")
    public void generateChatRoom() {
//        chatService.createChatRoom();
    }

    @PostMapping("/list")
    public ResponseEntity<List<ChatRoomInfoDTO>> extractChatList() {
        String email = userDetailsServiceImpl.getUserNameByAccessToken();
        List<ChatRoomInfoDTO> chatRoomInfos = chatService.findChatRoomInfoByEmail(email);
        return new ResponseEntity<>(chatRoomInfos, HttpStatus.OK);
    }

    @PostMapping("/message-list")
    public ResponseEntity<List<ChatMessage>> getMessageListByChatRoomId(
            @RequestBody MessageListRequestDTO messageListRequestDTO) {
        List<ChatMessage> list = chatService.findChatMessageByChatRoomId(messageListRequestDTO);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
