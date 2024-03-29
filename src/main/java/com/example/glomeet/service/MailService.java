package com.example.glomeet.service;

import com.example.glomeet.exception.InvalidSchoolEmailException;
import com.example.glomeet.repository.VerificationRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final VerificationRepository verificationRepository;

    public void sendRandomCode(String email) throws MessagingException {
        String toMail = email;
        String randomCode = generateRandomCode();
        try {
            validateSchoolEmail(toMail);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");
            messageHelper.setFrom(FROM_MAIL);
            messageHelper.setTo(toMail);
            messageHelper.setSubject(AUTH_MAIL_SUBJECT);
            messageHelper.setText(randomCode);

            mailSender.send(message);
            verificationRepository.save(email, randomCode);
        } catch (MessagingException e) {
            throw new MessagingException("메일 전송 중 오류");
        }
    }

    private String generateRandomCode() {
        Random random = new Random();
        int n = random.nextInt(1000000);
        return String.format("%06d", n);
    }


    private void validateSchoolEmail(String email) {
        if (!email.endsWith("ac.kr")) {
            throw new InvalidSchoolEmailException("학교 메일만 가능합니다");
        }
    }

}
