package com.example.glomeet.repository;

import java.util.Date;

public interface ChatMessageRepositoryCustom {
    void updateUnreadCountAfterDate(String roomId, Date date);
}
