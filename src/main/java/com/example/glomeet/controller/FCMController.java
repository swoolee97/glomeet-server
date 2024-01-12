package com.example.glomeet.controller;

import com.example.glomeet.dto.NotificationRequestDTO;
import com.example.glomeet.entity.FCMToken;
import com.example.glomeet.mapper.FCMMapper;
import com.example.glomeet.service.FCMService;
import com.google.firebase.messaging.FirebaseMessagingException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm")
public class FCMController {
    private final FCMMapper fcmMapper;
    private final FCMService fcmService;

    @PostMapping("/test")
    public void test(@RequestBody NotificationRequestDTO notificationRequestDTO)
            throws IOException, FirebaseMessagingException {
        fcmService.sendNotification(notificationRequestDTO);
        FCMToken fcmToken = fcmMapper.findTokenByEmail(notificationRequestDTO.getEmail());
    }

}
