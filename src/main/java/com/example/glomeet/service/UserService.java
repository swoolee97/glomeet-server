package com.example.glomeet.service;

import static com.example.glomeet.constants.RedisConstants.ACTIVE_USER_PREFIX;
import static com.example.glomeet.constants.RedisConstants.ONLINE_USER_PREFIX;

import com.example.glomeet.mapper.MatchingMapper;
import com.example.glomeet.mapper.MeetingMapper;
import com.example.glomeet.mapper.UserMapper;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
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
        Set<String> onlineUsers = setOperations.members(ONLINE_USER_PREFIX);
        return onlineUsers;
    }

    public String findNickNameByEmail(String email) {
        return userMapper.findNickNameByEmail(email);
    }

    public void addActiveUserInChatRoom(String roomId, String email) {
        setOperations = redisTemplate.opsForSet();
        String key = ACTIVE_USER_PREFIX + roomId;
        setOperations.add(key, email);
    }

    public void removeActiveUserInChatRoom(String roomId, String email) {
        setOperations = redisTemplate.opsForSet();
        String key = ACTIVE_USER_PREFIX + roomId;
        setOperations.remove(key, email);
    }
}
