package com.example.glomeet.mapper;

import com.example.glomeet.dto.ChatInfoDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MatchingMapper {
    void insertMatchingRoom();

    void deleteMatchingRoom();

    void insertMatchingUser(int chatRoomId, String userEmail);

    List<String> findMatchingRoomByEmail(String email);

    List<ChatInfoDTO> findMatchingRoomInfoByEmail(String email);

}
