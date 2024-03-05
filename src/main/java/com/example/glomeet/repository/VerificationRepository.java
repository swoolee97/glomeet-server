package com.example.glomeet.repository;

import com.example.glomeet.controller.AuthController.VerificationCheckDTO;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class VerificationRepository {
    private final int VERIFICATION_EXPIRE = 60 * 3;
    private final String VERIFICATION_PREFIX = "verificationEmail:";

    private RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> valueOperations;

    public VerificationRepository(final RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    public void save(final String email, final String randomCode) {
        System.out.println(randomCode);
        valueOperations.set(VERIFICATION_PREFIX + email,
                randomCode);
        redisTemplate.expire(VERIFICATION_PREFIX + email, VERIFICATION_EXPIRE,
                TimeUnit.SECONDS);
    }

    public void delete(String email) {
        redisTemplate.delete(VERIFICATION_PREFIX + email);
    }

    public boolean checkByEmailAndRandomCode(VerificationCheckDTO verificationCheckDTO) {
        String randomCode = valueOperations.get(VERIFICATION_PREFIX + verificationCheckDTO.getEmail());
        if (!Objects.isNull(randomCode) && verificationCheckDTO.getRandomCode().equals(randomCode.toString())) {
            delete(verificationCheckDTO.getEmail());
            return true;
        }
        return false;
    }


}
