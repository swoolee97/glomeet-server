package com.example.glomeet.controller;

import com.example.glomeet.dto.PushMessageRequestDTO;
import com.example.glomeet.service.FCMService;
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
    private final FCMService fcmService;

    @PostMapping("/test")
    public void test(@RequestBody PushMessageRequestDTO pushMessageRequestDTO)
            throws IOException {
        fcmService.sendPushMessage(pushMessageRequestDTO);
    }

}
