package com.example.glomeet.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RequiredArgsConstructor
@Getter
@RedisHash(value = "refreshToken", timeToLive = 1000 * 60 * 60 * 24 * 15)
public class RefreshToken {
    private final String email;
    private final String refreshToken;
}
