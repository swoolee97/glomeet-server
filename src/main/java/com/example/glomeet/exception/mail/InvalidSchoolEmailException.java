package com.example.glomeet.exception.mail;

public class InvalidSchoolEmailException extends RuntimeException {
    public InvalidSchoolEmailException(String message) {
        super(message);
    }
}
