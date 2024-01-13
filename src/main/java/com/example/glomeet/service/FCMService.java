package com.example.glomeet.service;

import com.example.glomeet.dto.NotificationRequestDTO;
import com.example.glomeet.entity.FCMToken;
import com.example.glomeet.exception.FCMTokenNotFoundException;
import com.example.glomeet.mapper.FCMMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMService {
    private final ObjectMapper objectMapper;
    private final FCMMapper fcmMapper;
    @Value("${fcm.project.id}")
    private String PROJECT_ID;

    public void sendNotification(@NonNull NotificationRequestDTO notificationRequestDTO)
            throws IOException {
        OkHttpClient client = new OkHttpClient();
        Optional<FCMToken> targetToken = fcmMapper.findTokenByEmail(notificationRequestDTO.getEmail());
        if (targetToken.isEmpty()) {
            log.error("토큰이 존재하지 않음 : " + notificationRequestDTO.getEmail());
            throw new FCMTokenNotFoundException("토큰이 존재하지 않는 사용자 : 알림을 보낼 수 없음");
        }
        String message = makeMessage(targetToken.get().getToken(), notificationRequestDTO.getTitle(),
                notificationRequestDTO.getBody());
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8")
                .post(requestBody)
                .url("https://fcm.googleapis.com/v1/projects/" + PROJECT_ID + "/messages:send")
                .build();
        Response response = client.newCall(request).execute();
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
        FCMMessage fcmMessage = FCMMessage.builder()
                .message(FCMMessage.Message.builder()
                        .notification(FCMMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .build())
                        .token(targetToken)
                        .build())
                .build();
        System.out.println(fcmMessage.toString());
        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(
                "firebase/glomeet-f03b2-firebase-adminsdk-ghmi5-16c34175e5.json");

        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(classPathResource.getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    @Builder
    @Getter
    @AllArgsConstructor
    private static class FCMMessage {
        private Message message;

        @Builder
        @AllArgsConstructor
        @Getter
        public static class Message {
            private Notification notification;
            private String token;
        }

        @Builder
        @AllArgsConstructor
        @Getter
        public static class Notification {
            private String title;
            private String body;
        }
    }
}
