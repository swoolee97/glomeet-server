package com.example.glomeet.service;

import com.example.glomeet.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
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
        }
        return message;
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
