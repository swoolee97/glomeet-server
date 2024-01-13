package com.example.glomeet.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FCMTokenNotFoundExceptionHandler {

    @ExceptionHandler(FCMTokenNotFoundException.class)
    public ResponseEntity FCMTokenNotFoundExceptionHandler(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
