package com.example.glomeet.controller;

import com.example.glomeet.dto.ResponseBody;
import com.example.glomeet.exception.InvalidSchoolEmailException;
import com.example.glomeet.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
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
    public ResponseEntity sendRandomCode(@RequestBody @Valid MailRequest mailRequest) {
        String email = mailRequest.getEmail();
        try {
            mailService.sendRandomCode(email);
            return new ResponseEntity(HttpStatus.OK);
        } catch (InvalidSchoolEmailException e) { // 학교 메일 아닐 때 예외처리
            return new ResponseEntity(ResponseBody.builder()
                    .message(e.getMessage())
                    .build(), HttpStatus.BAD_REQUEST);
        } catch (MessagingException e) {        // 메일 전송 알 수 없는 오류
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @Getter
    public static class MailRequest {
        @NotNull
        private String email;
    }

}
