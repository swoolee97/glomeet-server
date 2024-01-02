package com.example.glomeet.controller;

import com.example.glomeet.service.UserService;
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
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signUp")
    public void signUp(@RequestBody @NonNull SignUpRequest signUpRequest) {

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

    @Getter
    private static class CheckRequest {
        private String email;
        private String nickName;
    }

    @Getter
    private static class SignUpRequest {
        @NonNull
        private String email;
        @NonNull
        private String password;
        @NonNull
        private String nickName;
    }

}
