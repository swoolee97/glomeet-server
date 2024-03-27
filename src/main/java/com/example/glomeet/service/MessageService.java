package com.example.glomeet.service;

import com.example.glomeet.dto.MessageListRequestDTO;
import com.example.glomeet.dto.PushMessageRequestDTO;
import com.example.glomeet.mongo.model.ChatMessage;
import com.example.glomeet.repository.ChatMessageRepositoryCustomImpl;
import com.example.glomeet.repository.LastReadTimeRepository;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final RedisTemplate<String, String> redisTemplate;
    private SetOperations<String, String> setOperations;
    private final ChatMessageRepositoryCustomImpl chatMessageRepositoryCustom;
    private final UserService userService;
    private final FCMService fcmService;
    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달
    private final LastReadTimeRepository lastReadTimeRepository;

    private static final String COUNT_ACTIVE_USER_PREFIX = "activeUsers:";

    public int getActiveUserCount(ChatMessage message) {
        String key = COUNT_ACTIVE_USER_PREFIX + message.getRoomId();
        setOperations = redisTemplate.opsForSet();
        return setOperations.size(key).intValue();
    }

    public void updateUnReadUserCount(MessageListRequestDTO messageListRequestDTO) {
        String id = messageListRequestDTO.getRoomId();
        Date lastReadAt = messageListRequestDTO.getLastReadAt();
        chatMessageRepositoryCustom.updateUnreadCountAfterDate(id, lastReadAt);
    }

    public Set<String> findInRoomUsers(String roomId) {
        setOperations = redisTemplate.opsForSet();
        return setOperations.members(COUNT_ACTIVE_USER_PREFIX + roomId);
    }

    public void sendInfoMessage(String roomId, ChatMessage message) {
        template.convertAndSend("/sub/chat/" + roomId, message);
    }

    public void sendMessage(String roomId, ChatMessage message) {
        // 현재 방에 접속해 있는 사용자들
        Set<String> currentChatUsers = findInRoomUsers(roomId);
        // 해당 방의 전체 사용자들
        List<String> allChatUsers = userService.findUsersByRoomId(roomId);
        // 현재 로그인한 모든 사용자들
        Set<String> onlineUsers = userService.findActiveUsers();

        // 채팅방에 이벤트 보내기
        template.convertAndSend("/sub/chat/" + roomId, message);

        // 새로운 메시지 이벤트를 받을 사용자들(웹소켓으로 전송)
        Set<String> newMessageEventUsers = new HashSet<>(allChatUsers);
        newMessageEventUsers.removeAll(currentChatUsers);
        newMessageEventUsers.retainAll(onlineUsers);

        // 푸시 알림을 받을 사용자들
        Set<String> pushNotificationUsers = new HashSet<>(allChatUsers);
        pushNotificationUsers.removeAll(currentChatUsers);
        pushNotificationUsers.removeAll(onlineUsers);

        // 새로운 메시지 이벤트를 받을 사용자들에게 웹소켓으로 메시지 전송
        newMessageEventUsers.forEach(email -> template.convertAndSend("/sub/new-message/" + email, message));

        // 푸시 알림을 받을 사용자들에게 푸시 알림 전송
        pushNotificationUsers.forEach(email -> {
            try {
                fcmService.sendPushMessage(
                        new PushMessageRequestDTO(email, message.getSenderEmail(), message.getMessage()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


}
