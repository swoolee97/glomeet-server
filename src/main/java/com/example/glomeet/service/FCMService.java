package com.example.glomeet.service;

import com.example.glomeet.dto.PushMessageRequestDTO;
import com.example.glomeet.mapper.FCMMapper;
import com.example.glomeet.service.FCMService.FCMMessage.Data;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FCMService {
    private final ObjectMapper objectMapper;
    private final FCMMapper fcmMapper;
    @Value("${fcm.project.id}")
    private String PROJECT_ID;

    public void sendPushMessage(@NonNull PushMessageRequestDTO pushMessageRequestDTO)
            throws IOException {
        OkHttpClient client = new OkHttpClient();
        String targetToken = fcmMapper.findTokenByEmail(pushMessageRequestDTO.getEmail()).getToken();
        String message = makeMessage(targetToken, pushMessageRequestDTO.getTitle(), pushMessageRequestDTO.getBody());
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8")
                .post(requestBody)
                .url("https://fcm.googleapis.com/v1/projects/" + PROJECT_ID + "/messages:send")
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
        System.out.println(response.body());
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
        FCMMessage fcmMessage = FCMMessage.builder()
                .message(FCMMessage.Message.builder()
                        .data(Data.builder()
                                .title(title)
                                .body(body)
                                .build())
                        .token(targetToken)
                        .build())
                .build();
        System.out.println("@@@@@");
        System.out.println(fcmMessage.toString());
        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(
                "firebase/glomeet-212d3-firebase-adminsdk-9mrhi-70a0a06613.json");

        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(classPathResource.getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class FCMMessage {
        private Message message;

        @Builder
        @AllArgsConstructor
        @Getter
        public static class Message {
            private Data data;
            private String token;
        }

        @Builder
        @AllArgsConstructor
        @Getter
        public static class Data {
            private String title;
            private String body;
        }
    }
}
