package com.example.glomeet.controller;

import com.example.glomeet.service.MailService;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/auth")
    public void sendRandomCode(@RequestBody @NonNull MailRequest mailRequest) {
        String email = mailRequest.getEmail();
        String randomCode = mailRequest.getRandomCode();
        mailService.sendRandomCode(email, randomCode);
    }

    @Getter
    private static class MailRequest {
        @NonNull
        String email;
        @NonNull
        String randomCode;
    }

}
