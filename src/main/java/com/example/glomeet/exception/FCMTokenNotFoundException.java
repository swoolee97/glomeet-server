package com.example.glomeet.exception;

public class FCMTokenNotFoundException extends RuntimeException {
    public FCMTokenNotFoundException(String message) {
        super(message);
    }
}
