package com.example.glomeet.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${spring.mail.username}")
    private String FROM_MAIL;
    private static final String AUTH_MAIL_SUBJECT = "글로밋 가입 인증번호";

    private final JavaMailSender mailSender;

    public ResponseEntity sendRandomCode(String toMail, String randomCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");
            messageHelper.setFrom(FROM_MAIL);
            messageHelper.setTo(toMail);

            messageHelper.setSubject(AUTH_MAIL_SUBJECT);
            messageHelper.setText(randomCode);
            mailSender.send(message);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
