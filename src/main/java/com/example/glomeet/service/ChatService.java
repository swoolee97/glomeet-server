package com.example.glomeet.service;

import com.example.glomeet.controller.ChatController;
import com.example.glomeet.controller.ChatController.TranslationResponseDTO;
import com.example.glomeet.controller.ChatController.TranslationRequestDTO;
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

    public String translateToEnglish(TranslationRequestDTO translationRequestDTO) {
        String authKey = "989d8dcc-8b6a-49d0-9ee7-ea8dff6d4933:fx";
        translator = new Translator(authKey);
        try {
            TextResult result = translator.translateText(translationRequestDTO.getText(), null, "EN");
            translationRequestDTO.setText(result.getText());
            return translationRequestDTO.getText();
        } catch (Exception e) {
            e.printStackTrace();
            return "Translation failed: " + e.getMessage();
        }
    }
}
