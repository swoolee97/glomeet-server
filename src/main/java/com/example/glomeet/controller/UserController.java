package com.example.glomeet.controller;

import com.example.glomeet.auth.JwtTokenProvider;
import com.example.glomeet.dto.TokenDTO;
import com.example.glomeet.dto.UserDTO;
import com.example.glomeet.service.UserService;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signUp")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody @NonNull UserDTO userDTO) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String cryptedPassword = encoder.encode(userDTO.getPassword());
        userDTO.setPassword(cryptedPassword);
        boolean result = userService.signUp(userDTO);
        return new ResponseEntity<>(new SignUpResponse(result), HttpStatus.OK);
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody @NonNull UserDTO userDTO) {
        Authentication authentication = userService.signIn(userDTO);
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        return new ResponseEntity<>(new TokenDTO(accessToken), HttpStatus.OK);
    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    private ResponseEntity handleInvalidLoginRequest() {
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/emailCheck")
    public ResponseEntity<CheckResponse> emailCheck(@RequestBody CheckRequest checkRequest) {
        boolean isValid = userService.isValidEmail(checkRequest.getEmail());
        return new ResponseEntity<>(new CheckResponse(isValid), HttpStatus.OK);
    }

    @PostMapping("/nickNameCheck")
    public ResponseEntity<CheckResponse> nickNameCheck(@RequestBody CheckRequest checkRequest) {
        boolean isValid = userService.isValidNickName(checkRequest.getNickName());
        return new ResponseEntity<>(new CheckResponse(isValid), HttpStatus.OK);
    }

    @RequiredArgsConstructor
    @Getter
    private static class CheckResponse {
        private final boolean isValid;
    }

    @RequiredArgsConstructor
    @Getter
    private static class SignUpResponse {
        private final boolean isComplete;
    }

    @Getter
    private static class CheckRequest {
        private String email;
        private String nickName;
    }

}
