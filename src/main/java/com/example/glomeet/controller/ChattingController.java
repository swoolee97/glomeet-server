package com.example.glomeet.controller;

import com.example.glomeet.dto.MessageListRequestDTO;
import com.example.glomeet.mongo.model.ChatMessage;
import com.example.glomeet.repository.LastReadTimeRepository;
import com.example.glomeet.service.ChattingService;
import com.example.glomeet.service.MessageService;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/chat")
@RestController
@RequiredArgsConstructor
public class ChattingController {

    private final ChattingService chattingService;
    private final MessageService messageService;
    private final LastReadTimeRepository lastReadTimeRepository;

    @PostMapping("/my")
    public ResponseEntity<?> getMyChatList() {
        List<String> chatIdList = chattingService.getMyChatList();
        return ResponseEntity.ok(chatIdList);
    }

    @PostMapping("/message-list")
    public ResponseEntity<?> getMessageListByChatRoomId(
            @RequestBody MessageListRequestDTO messageListRequestDTO) {
        Date lastReadAt = lastReadTimeRepository.findLastReadTimeByRoomIdAndEmail(messageListRequestDTO);
        messageListRequestDTO.setLastReadAt(lastReadAt);
        chattingService.commitMessagesToDatabase(messageListRequestDTO);
        System.out.println(messageListRequestDTO.getLastReadAt());
        messageService.updateUnReadUserCount(messageListRequestDTO);
        List<ChatMessage> list = chattingService.findMatchingMessageByChatRoomId(messageListRequestDTO);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/commit")
    public void commitMessageToDatabase(@RequestBody Map<String, String> map) {
        String id = map.get("id");
        chattingService.commitMessagesToDatabase(new MessageListRequestDTO(id, null));
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class EnterChatRoomRequestDTO {
        private List<ChatMessage> list;
        private int activeUserCount;
    }
}
