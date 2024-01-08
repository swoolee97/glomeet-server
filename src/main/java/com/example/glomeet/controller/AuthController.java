package com.example.glomeet.controller;

import com.example.glomeet.auth.JwtTokenProvider;
import com.example.glomeet.dto.TokenDTO;
import com.example.glomeet.dto.UserDTO;
import com.example.glomeet.service.UserService;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody @NonNull UserDTO userDTO) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String cryptedPassword = encoder.encode(userDTO.getPassword());
        userDTO.setPassword(cryptedPassword);
        Map<String, Boolean> body = new HashMap<>();
        boolean result = userService.signUp(userDTO);
        body.put("result", result);
        if (result) {
            return new ResponseEntity<>(new SignUpResponse(result), HttpStatus.OK);
        }
        return new ResponseEntity<>(new SignUpResponse(result), HttpStatus.CONFLICT);
    }

    @PostMapping("/test")
    public ResponseEntity test(@RequestBody @NonNull UserDTO userDTO) {
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody @NonNull UserDTO userDTO) {
        Authentication authenticationToken = userService.signIn(userDTO);
        //authenticationManager는 임시방편으로 여기서 실행. userService에서 하면 순환참조 오류나서
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        System.out.println(authentication);
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        return new ResponseEntity<>(new TokenDTO(accessToken), HttpStatus.OK);
    }

    @PostMapping("/emailCheck")
    public ResponseEntity<CheckResponse> emailCheck(@RequestBody CheckRequest checkRequest) {
        boolean isValid = userService.isValidEmail(checkRequest.getEmail());
        if (isValid) {
            return new ResponseEntity<>(new CheckResponse(isValid), HttpStatus.OK);
        }
        return new ResponseEntity<>(new CheckResponse(isValid), HttpStatus.CONFLICT);
    }

    @PostMapping("/nickNameCheck")
    public ResponseEntity<CheckResponse> nickNameCheck(@RequestBody CheckRequest checkRequest) {
        boolean isValid = userService.isValidNickName(checkRequest.getNickName());
        if (isValid) {
            return new ResponseEntity<>(new CheckResponse(isValid), HttpStatus.OK);
        }
        return new ResponseEntity<>(new CheckResponse(isValid), HttpStatus.CONFLICT);
    }

    @ResponseBody
    @RequiredArgsConstructor
    @Getter
    private static class CheckResponse {
        private final boolean isValid;
    }

    @Getter
    @ResponseBody
    @RequiredArgsConstructor
    private static class SignUpResponse {
        private final boolean isComplete;
    }

    @Getter
    private static class CheckRequest {
        private String email;
        private String nickName;
    }

}
