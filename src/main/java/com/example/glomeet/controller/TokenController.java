package com.example.glomeet.controller;

import com.example.glomeet.dto.TokenDTO;
import com.example.glomeet.service.TokenService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/token")
@RestController
public class TokenController {
    private final TokenService tokenService;

    // 엑세스 토큰 만료됐을 때 리프레시토큰 대조하는 api
    @PostMapping("/re-issue")
    public ResponseEntity<TokenDTO> checkRefreshToken(
            @RequestBody ReIssueRefreshTokenRequestDTO reIssueRefreshTokenRequestDTO)
            throws BadRequestException {
        TokenDTO reIssuedTokens = tokenService.reIssueAccessToken(reIssueRefreshTokenRequestDTO.getEmail(),
                reIssueRefreshTokenRequestDTO.getRefreshToken());
        return new ResponseEntity(reIssuedTokens, HttpStatus.OK);
    }

    @PostMapping("/checkToken")
    public void checkAccessToken() {

    }

    @Getter
    @RequiredArgsConstructor
    public static class ReIssueRefreshTokenRequestDTO {
        private final String email;
        private final String refreshToken;
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity InvalidRefreshTokenExceptionHandler() {
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

}
