package com.example.glomeet.controller;

import com.example.glomeet.dto.MatchingRoomInfoDTO;
import com.example.glomeet.dto.MessageListRequestDTO;
import com.example.glomeet.mongo.model.MatchingMessage;
import com.example.glomeet.service.MatchingService;
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
@RequestMapping("/matching")
public class MatchingController {

    private final MatchingService matchingService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @PostMapping("/generate")
    public void generateChatRoom() {
//        chatService.createChatRoom();
    }

    @PostMapping("/list")
    public ResponseEntity<List<MatchingRoomInfoDTO>> extractMatchingList() {
        String email = userDetailsServiceImpl.getUserNameByAccessToken();
        List<MatchingRoomInfoDTO> matchingRoomInfos = matchingService.findMatchingRoomInfoByEmail(email);
        List<MatchingRoomInfoDTO> lastMessages = matchingService.findLastMessageByMatchingRoomId(matchingRoomInfos);
        return new ResponseEntity<>(lastMessages, HttpStatus.OK);
    }

    @PostMapping("/message-list")
    public ResponseEntity<List<MatchingMessage>> getMessageListByChatRoomId(
            @RequestBody MessageListRequestDTO messageListRequestDTO) {
        List<MatchingMessage> list = matchingService.MatchingMessageByChatRoomId(messageListRequestDTO);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
