package com.example.glomeet.mapper;

public interface RefreshTokenMapper {
    void insertToken(String refreshToken, String userEmail);

    int deleteToken(String userEmail);
}
