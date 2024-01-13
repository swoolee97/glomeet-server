package com.example.glomeet.mapper;

import com.example.glomeet.entity.FCMToken;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FCMMapper {
    Optional<FCMToken> findTokenByEmail(String email);
}
