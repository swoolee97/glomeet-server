package com.example.glomeet.repository;

import com.example.glomeet.entity.RefreshToken;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepository {
    private final long REFRESH_TOKEN_EXPIRE = 60 * 60 * 24 * 15;
    private final String REFRESH_TOKEN_PREFIX = "refreshToken:";

    private RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOperations;

    public RefreshTokenRepository(final RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    public void save(final RefreshToken refreshToken) {
        valueOperations.set(REFRESH_TOKEN_PREFIX + refreshToken.getEmail(), refreshToken.getRefreshToken());
        redisTemplate.expire(REFRESH_TOKEN_PREFIX + refreshToken.getEmail(), REFRESH_TOKEN_EXPIRE,
                TimeUnit.SECONDS);
    }

    public void delete(final String email) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + email);
    }

    public Optional<RefreshToken> findByEmail(String email) {
        Object refreshToken = valueOperations.get(REFRESH_TOKEN_PREFIX + email);
        if (Objects.isNull(refreshToken)) {
            return Optional.empty();
        }
        return Optional.of(new RefreshToken(email, refreshToken.toString()));
    }

}
