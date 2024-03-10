package com.example.glomeet.service;

import com.example.glomeet.dto.ChatRoomInfoDTO;
import com.example.glomeet.dto.MessageListRequestDTO;
import com.example.glomeet.entity.ChatMessage;
import com.example.glomeet.mapper.ChatMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.deepl.api.*;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMapper chatMapper;
    private String deeplApiKey;
    Translator translator;

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

    public List<String> findChatRoomByEmail(String email) {
        return chatMapper.findChatRoomByEmail(email);
    }

    public List<ChatRoomInfoDTO> findChatRoomInfoByEmail(String email) {
        return chatMapper.findChatRoomInfoByEmail(email);
    }

    public List<ChatMessage> findChatMessageByChatRoomId(MessageListRequestDTO messageListRequestDTO) {
        return chatMapper.findChatMessages(messageListRequestDTO);
    }

    public String translateToEnglish(String text) {
        try {
            TextResult result = translator.translateText(text, null, "EN");
            return result.getText();
        } catch (Exception e) {
            throw new RuntimeException("Translation error", e);
        }
    }
}
