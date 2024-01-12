package com.example.glomeet.mapper;

import com.example.glomeet.entity.FCMToken;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FCMMapper {
    FCMToken findTokenByEmail(String email);
}
