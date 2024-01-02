package com.example.glomeet.service;

import com.example.glomeet.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    public boolean isValidEmail(String email) {
        int count = userMapper.emailCheck(email);
        if (count > 0) {
            return false;
        }
        return true;
    }

    public boolean isValidNickName(String nickName) {
        int count = userMapper.nickNameCheck(nickName);
        if (count > 0) {
            return false;
        }
        return true;
    }
}
