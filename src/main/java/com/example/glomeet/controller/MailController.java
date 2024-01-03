package com.example.glomeet.controller;

import com.example.glomeet.exception.mail.InvalidSchoolEmailException;
import com.example.glomeet.service.MailService;
import jakarta.mail.MessagingException;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity sendRandomCode(@RequestBody @NonNull MailRequest mailRequest) {
        String email = mailRequest.getEmail();
        String randomCode = mailRequest.getRandomCode();
        try {
            mailService.sendRandomCode(email, randomCode);
            return new ResponseEntity(HttpStatus.OK);
        } catch (InvalidSchoolEmailException e) { // 학교 메일 아닐 때 예외처리
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        } catch (MessagingException e) {        // 메일 전송 알 수 없는 오류
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

//    @ExceptionHandler(InvalidSchoolEmailException.class)
//    private ResponseEntity<String> handleInvalidEmail(Exception e) {
//        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
//    }

    @Getter
    private static class MailRequest {
        @NonNull
        String email;
        @NonNull
        String randomCode;
    }

}
