package com.example.glomeet.service;

import com.example.glomeet.dto.ChatRoomInfoDTO;
import com.example.glomeet.entity.ChatMessage;
import com.example.glomeet.mapper.ChatMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMapper chatMapper;

    public void createChatRoom(int chatRoomId, List<String> emails) {
        chatMapper.insertChatRoom();
        emails.forEach(email -> chatMapper.insertChatUser(chatRoomId, email));
    }

    public void saveChatMessage(ChatMessage chatMessage) {
        chatMapper.insertChatMessage(chatMessage);
    }

    public void deleteChatRoom() {
        chatMapper.deleteChatRoom();
    }

    public List<Integer> findChatRoomByEmail(String email) {
        return chatMapper.findChatRoomByEmail(email);
    }

    public List<ChatRoomInfoDTO> findChatRoomInfoByEmail(String email) {
        return chatMapper.findChatRoomInfoByEmail(email);
    }
}
