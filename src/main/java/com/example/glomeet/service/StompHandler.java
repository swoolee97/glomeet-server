package com.example.glomeet.service;

import static com.example.glomeet.constants.RedisConstants.ONLINE_USER_PREFIX;
import static com.example.glomeet.constants.WebSocketConstants.CHAT_ROOM_DESTINATION;
import static com.example.glomeet.constants.WebSocketConstants.NEW_MESSAGE_DESTINATION;

import com.example.glomeet.auth.JwtTokenProvider;
import com.example.glomeet.dto.UpdateLastReadTimeDTO;
import com.example.glomeet.repository.LastReadTimeRepository;
import java.sql.Date;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private SetOperations<String, String> setOperations;
    private final UserService userService;
    private final LastReadTimeRepository lastReadTimeRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        setOperations = redisTemplate.opsForSet();
        // 메시지의 구독 명령이 CONNECT인 경우에만 실행
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String accessToken = accessor.getFirstNativeHeader("accessToken");
            if (accessToken == null) {
                throw new IllegalStateException("토큰을 찾을 수 없습니다");
            }
            if (!jwtTokenProvider.validateToken(accessToken)) {
                // 에러 던지기
                log.info("유효하지 않은 토큰");
            }
        } else if (StompCommand.UNSUBSCRIBE.equals(accessor.getCommand())) {
            handleUnsubscribe(accessor);
        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            handleSubscribe(accessor);
        }
        return message;
    }

    private void handleUnsubscribe(StompHeaderAccessor accessor) {
        String email = accessor.getFirstNativeHeader("email");
        String destination = accessor.getFirstNativeHeader("destination");
        if (destination.startsWith(CHAT_ROOM_DESTINATION)) {
            String roomId = destination.substring(CHAT_ROOM_DESTINATION.length());
            userService.removeActiveUserInChatRoom(roomId, email);
            lastReadTimeRepository.updateLastReadTime(
                    new UpdateLastReadTimeDTO(roomId, email, Date.from(Instant.now())));
        } else if (destination.startsWith(NEW_MESSAGE_DESTINATION)) {
            setOperations.remove(ONLINE_USER_PREFIX, email);
        }
    }

    private void handleSubscribe(StompHeaderAccessor accessor) {
        String destination = accessor.getFirstNativeHeader("destination");
        if (destination.startsWith(CHAT_ROOM_DESTINATION)) {
            String roomId = destination.substring(CHAT_ROOM_DESTINATION.length());
            String email = accessor.getFirstNativeHeader("email");
            userService.addActiveUserInChatRoom(roomId, email);
        } else if (destination.startsWith(NEW_MESSAGE_DESTINATION)) {
            String email = destination.substring(NEW_MESSAGE_DESTINATION.length());
            setOperations.add(ONLINE_USER_PREFIX, email);
        }
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {

    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        System.out.println(channel);
        return true;
    }

}
