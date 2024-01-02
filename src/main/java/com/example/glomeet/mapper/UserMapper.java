package com.example.glomeet.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    public int emailCheck(@Param("email") String email);

    public int nickNameCheck(@Param("nickName") String nickName);
}
