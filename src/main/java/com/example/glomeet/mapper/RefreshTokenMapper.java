package com.example.glomeet.mapper;

public interface RefreshTokenMapper {
    void insertToken(String refreshToken, String userEmail);

    int deleteToken(String userEmail);

    String findTokenByEmail(String email);

    void updateRefreshToken(String email, String refreshToken);
}
