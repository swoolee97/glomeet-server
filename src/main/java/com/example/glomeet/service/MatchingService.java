package com.example.glomeet.service;

import com.example.glomeet.mapper.MatchingMapper;
import com.example.glomeet.repository.ChatMessageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private static final String LAST_MESSAGE_PREFIX = "lastMessage:";
    private static final String MESSAGE_LIST_PREFIX = "chat:";
    private final MatchingMapper matchingMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> valueOperations;
    private ListOperations<String, Object> listOperations;
    private final ChatMessageRepository chatMessageRepository;
    private final UserDetailsServiceImpl userDetailsService;

    public void createChatRoom(int chatRoomId, List<String> emails) {
        matchingMapper.insertMatchingRoom();
        emails.forEach(email -> matchingMapper.insertMatchingUser(chatRoomId, email));
    }

    public void deleteChatRoom() {
        matchingMapper.deleteMatchingRoom();
    }

    public List<String> findChatRoomByEmail(String email) {
        return matchingMapper.findMatchingRoomByEmail(email);
    }

}
