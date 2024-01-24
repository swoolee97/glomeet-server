package com.example.glomeet.mapper;

import com.example.glomeet.entity.FCMToken;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FCMMapper {
    Optional<FCMToken> findTokenByEmail(String email);

    int deleteTokenByEmail(String email);

    int insertToken(String email, String token);

    boolean countTokenByEmail(String email);
}
