package com.example.glomeet.service;

import com.example.glomeet.dto.UserDTO;
import com.example.glomeet.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@EnableWebSecurity
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final CustomUserDetailsImpl customUserDetails;
    private final PasswordEncoder encoder;

    public boolean signUp(UserDTO userDTO) {
        // 두 유저가 같은 이메일로 동시에 회원가입한다면 오류가 날 수 있기 때문에
        // 중복확인을 한 번 더 수행해야함.
        boolean isUniqueEmail = userMapper.emailCheck(userDTO.getEmail()) == 0;
        boolean isUniqueNickName = userMapper.nickNameCheck(userDTO.getNickName()) == 0;
        if (isUniqueEmail && isUniqueNickName) {
            userMapper.insertUser(userDTO);
            return true;
        }
        return false;
    }

    public Authentication signIn(UserDTO userDTO) {
        UserDetails user = customUserDetails.loadUserByUsername(userDTO.getEmail());
        if (!encoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다");
        }

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                userDTO.getEmail(),
                userDTO.getPassword()
        );

        return authenticationToken;
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
