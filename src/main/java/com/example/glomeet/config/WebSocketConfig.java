package com.example.glomeet.config;

import com.example.glomeet.WebSocketHandShakeInterceptor;
import com.example.glomeet.service.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker
@Configuration
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;
    private final WebSocketHandShakeInterceptor handShakeInterceptor;

    // 웹소켓 연결할 때 토큰으로 인증하게하는 인터셉터
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub"); // 이 경로로 요청이 온 것은 broker에서 가공 후 전달
        registry.setApplicationDestinationPrefixes("/pub"); // 이 경로로 온 것은 바로 구독자들에게 전달
    }

    //웹소켓 주소 설정하는 메서드
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat")
                .setAllowedOriginPatterns("*")
                .addInterceptors(handShakeInterceptor);
    }
}
