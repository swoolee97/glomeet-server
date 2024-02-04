package com.example.glomeet.exception;

import com.example.glomeet.dto.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String WRONG_USER_EXCEPTION_MESSAGE = "이메일 혹은 비밀번호가 일치하지 않습니다";
    private static final String INCLUDED_NULL_DATA_EXCEPTION_MESSAGE = "빈 값이 있습니다";

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity handleBadCredentialsException(Exception e) {
        return new ResponseEntity(ResponseBody.builder()
                .message(WRONG_USER_EXCEPTION_MESSAGE)
                .build(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleNullRequestException(Exception e) {
        return new ResponseEntity(ResponseBody.builder()
                .message(INCLUDED_NULL_DATA_EXCEPTION_MESSAGE)
                .build(), HttpStatus.BAD_REQUEST);
    }

}
