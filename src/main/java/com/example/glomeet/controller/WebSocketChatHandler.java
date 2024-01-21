package com.example.glomeet.controller;

import com.example.glomeet.entity.ChatMessage;
import com.example.glomeet.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    // 로그인 되어있는 유저(웹소켓에 접속되어있는 유저)
    private final Set<WebSocketSession> sessions;
    // 채팅방 마다 유저를 담음  /  key : chatRoomId, value : 채팅방 유저 집합
    private final Map<String, Set<WebSocketSession>> chatRoomSessions = new HashMap<>();
    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    // 로그인 시 웹소켓 연결
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String email = extractEmailFromSession(session);
        List<String> chatRoomIdList = chatService.findChatRoomByEmail(email);
        addSession(chatRoomIdList, session);
    }

    private void addSession(List<String> chatRoomIdList, WebSocketSession session) {
        for (String chatRoomId : chatRoomIdList) {
            chatRoomSessions.putIfAbsent(chatRoomId, new HashSet<>());
            chatRoomSessions.get(chatRoomId).add(session);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
        String chatRoomId = chatMessage.getChatRoomId();

        chatRoomSessions.get(chatRoomId).forEach(eachSession -> {
            try {
                eachSession.sendMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    private String extractEmailFromSession(WebSocketSession session) {
        URI uri = session.getUri();
        String email = uri.getQuery().replace("email=", "");
        return email;
    }
}
