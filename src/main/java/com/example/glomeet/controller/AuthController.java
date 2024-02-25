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
        boolean isValid = authService.isValidEmail(email);
        if (!isValid) {
            return new ResponseEntity<>("이메일이 유효하지 않습니다.", HttpStatus.FORBIDDEN);
        }

        String randomCode = "123456";
        try{
            mailService.sendRandomCode(email, randomCode); // 랜덤 코드를 이메일로 전송
        }catch(MessagingException e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);

    }



// 랜덤코드 만들고
// 존재하는 이메일인지 (UserMapper에서 따오기) --
// - 존재하지 않으면 403상태 보내기
// - 존재하는 이메일이면 인증코드 전송
// user db에서 update로 password변경

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
