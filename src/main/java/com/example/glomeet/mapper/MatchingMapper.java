package com.example.glomeet.mapper;

import com.example.glomeet.dto.MatchingRoomInfoDTO;
import com.example.glomeet.dto.MessageListRequestDTO;
import com.example.glomeet.mongo.model.ChatMessage;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MatchingMapper {
    void insertMatchingRoom();

    void deleteMatchingRoom();

    void insertMatchingUser(int chatRoomId, String userEmail);

    void insertChatMessages(List<ChatMessage> chatMessages);

    List<String> findMatchingRoomByEmail(String email);

    List<MatchingRoomInfoDTO> findMatchingRoomInfoByEmail(String email);

    List<ChatMessage> findMatchingMessages(MessageListRequestDTO messageListRequestDTO);
}
