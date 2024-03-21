package com.example.glomeet.repository;

import com.example.glomeet.dto.ChatInfoDTO;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ChatMessageRepositoryCustom {
    void updateUnreadCountAfterDate(String roomId, Date date);

    Map<String, Date> getLastReadMap(String email, List<ChatInfoDTO> chatInfos);
}
