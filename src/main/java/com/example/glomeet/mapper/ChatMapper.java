package com.example.glomeet.mapper;

import com.example.glomeet.dto.ChatRoomInfoDTO;
import com.example.glomeet.dto.MessageListRequestDTO;
import com.example.glomeet.entity.ChatMessage;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper {
    void insertChatRoom();

    void deleteChatRoom();

    void insertChatUser(int chatRoomId, String userEmail);

    void insertChatMessages(List<ChatMessage> chatMessages);

    List<String> findChatRoomByEmail(String email);

    List<ChatRoomInfoDTO> findChatRoomInfoByEmail(String email);

    List<ChatMessage> findChatMessages(MessageListRequestDTO messageListRequestDTO);
}
