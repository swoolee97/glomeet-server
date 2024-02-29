package com.example.glomeet.controller;

import com.example.glomeet.dto.UserDTO;
import com.example.glomeet.service.AuthService;
import com.example.glomeet.service.FCMService;
import com.example.glomeet.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final FCMService fcmService;
    private final MailService mailService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpDTO signUpDTO) {
        boolean result = authService.signUp(signUpDTO);
        if (result) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping("/test")
    public ResponseEntity test(@RequestBody @NotNull UserDTO userDTO) {
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody @Valid SignInDTO signInDTO) {
        Map<String, String> tokens = authService.signIn(signInDTO);
        fcmService.saveToken(signInDTO.getEmail(), signInDTO.getFcmToken());
        return ResponseEntity.ok().body(tokens);
    }

    @PostMapping("/signOut")
    public ResponseEntity<?> signOut(@RequestBody SignOutDTO signOutDTO) {
        boolean result = authService.signOut(signOutDTO.getEmail());
        fcmService.deleteFCMToken(signOutDTO.getEmail(), signOutDTO.getFcmToken());
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/emailCheck")
    public ResponseEntity emailCheck(@RequestBody CheckRequestDTO checkRequestDTO) {
        boolean isValid = authService.isValidEmail(checkRequestDTO.getEmail());
        if (isValid) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping("/nickNameCheck")
    public ResponseEntity nickNameCheck(@RequestBody CheckRequestDTO checkRequestDTO) {
        boolean isValid = authService.isValidNickName(checkRequestDTO.getNickName());
        if (isValid) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid CheckRequestDTO checkRequestDTO) {
        String email = checkRequestDTO.getEmail();
        boolean isInvalid = authService.isValidEmail(email); // 이메일이 DB에 없으면 true, 있으면 false 반환
        if (isInvalid) {
            // 이메일이 DB에 없으면 (즉, 유효하지 않은 이메일이면) 메일 전송을 시도하지 않고, OK 상태 반환
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            // 이메일이 DB에 있으면 (즉, 유효한 이메일이면) 랜덤 코드를 이메일로 전송
            String randomCode = "123456";
            try {
                mailService.sendRandomCode(email, randomCode);
            } catch(MessagingException e) {
                // 메일 전송 과정에서 오류가 발생하면, CONFLICT 상태 반환
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            // 메일 전송이 성공하면, OK 상태 반환
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }



// 랜덤코드 만들고
// 존재하는 이메일인지 (UserMapper에서 따오기) --
// - 존재하지 않으면 403상태 보내기
// - 존재하는 이메일이면 인증코드 전송
// user db에서 reset로 password변경

    @Getter
    private static class CheckRequestDTO {
        private String email;
        private String nickName;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public static class SignUpDTO {
        @NotNull
        private String email;
        @NotNull
        private String password;
        @NotNull
        private String nickName;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public static class SignInDTO {
        @NotNull
        private String email;
        @NotNull
        private String password;
        @NotNull
        private String fcmToken;
    }

    @Getter
    public static class SignOutDTO {
        @NotNull
        private String email;
        @NotNull
        private String fcmToken;
    }

}
