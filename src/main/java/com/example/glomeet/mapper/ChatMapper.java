package com.example.glomeet.mapper;

import com.example.glomeet.dto.ChatRoomInfoDTO;
import com.example.glomeet.entity.ChatMessage;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper {
    void insertChatRoom();

    void deleteChatRoom();

    void insertChatUser(int chatRoomId, String userEmail);

    void insertChatMessage(ChatMessage chatMessage);

    List<Integer> findChatRoomByEmail(String email);

    List<ChatRoomInfoDTO> findChatRoomInfoByEmail(String email);
}
