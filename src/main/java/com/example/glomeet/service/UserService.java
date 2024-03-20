package com.example.glomeet.service;

import com.example.glomeet.mapper.MatchingMapper;
import com.example.glomeet.mapper.MeetingMapper;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final MatchingMapper matchingMapper;
    private final MeetingMapper meetingMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private SetOperations<String, String> setOperations;

    public List<String> findUsersByRoomId(String roomId) {
        List<String> matchingUsers = matchingMapper.findUserEmailsByRoomId(roomId);
        if (matchingUsers.isEmpty()) {
            return meetingMapper.findMeetingUsersById(roomId);
        }
        return matchingUsers;
    }

    public Set<String> findActiveUsers() {
        setOperations = redisTemplate.opsForSet();
        Set<String> onlineUsers = setOperations.members("activeUsers");
        return onlineUsers;
    }
}
