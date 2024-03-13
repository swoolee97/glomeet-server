package com.example.glomeet.service;

import com.example.glomeet.mapper.MatchingMapper;
import com.example.glomeet.mapper.MeetingMapper;
import com.example.glomeet.mongo.model.ChatMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChattingService {
    private static final String LAST_MESSAGE_PREFIX = "lastMessage:";
    private static final String MESSAGE_LIST_PREFIX = "chat:";
    private final RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> valueOperations;
    private final UserDetailsServiceImpl userDetailsService;
    private final MatchingMapper matchingMapper;
    private final MeetingMapper meetingMapper;

    public List<String> getMyChatList() {
        String email = userDetailsService.getUserNameByAccessToken();
        List<String> matchingIdList = matchingMapper.findMatchingRoomByEmail(email);
        List<String> meetingIdList = meetingMapper.findMyMeetingsIdByEmail(email);
        matchingIdList.addAll(meetingIdList);
        return matchingIdList;
    }

    public void saveMessageToRedis(ChatMessage chatMessage) {
        addMessageToRedis(chatMessage);
        saveLastMessage(chatMessage);
    }

    public void saveLastMessage(ChatMessage chatMessage) {
        valueOperations = redisTemplate.opsForValue();
        valueOperations.set(LAST_MESSAGE_PREFIX + chatMessage.getRoomId(), chatMessage);
    }

    public void addMessageToRedis(ChatMessage chatMessage) {
        redisTemplate.opsForList()
                .rightPush(MESSAGE_LIST_PREFIX + chatMessage.getRoomId(), chatMessage);
    }
}
