package com.example.glomeet.service;

import com.example.glomeet.dto.UserDTO;
import com.example.glomeet.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@EnableWebSecurity
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserMapper userMapper;

    public boolean signUp(UserDTO userDTO) {
        int result = userMapper.insertUser(userDTO);
        if (result == 0) {
            return false;
        }
        return true;
    }

    public Authentication signIn(UserDTO userDTO) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UserDTO user = userMapper.findUserByEmail(userDTO.getEmail());
        if (user == null) {
            throw new UsernameNotFoundException("존재하지 않는 사용자입니다");
        }
        if (!encoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다");
        }

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                userDTO.getEmail(),
                userDTO.getPassword()
        );
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return authentication;
    }

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
