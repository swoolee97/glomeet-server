package com.example.glomeet.exception;

public class InvalidSchoolEmailException extends RuntimeException {
    public InvalidSchoolEmailException(String message) {
        super(message);
    }
}
