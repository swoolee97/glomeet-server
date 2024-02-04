package com.example.glomeet.service;

import com.example.glomeet.auth.JwtTokenProvider;
import com.example.glomeet.controller.AuthController.SignInDTO;
import com.example.glomeet.controller.AuthController.SignUpDTO;
import com.example.glomeet.mapper.FCMMapper;
import com.example.glomeet.mapper.RefreshTokenMapper;
import com.example.glomeet.mapper.UserMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final RefreshTokenMapper refreshTokenMapper;
    private final UserDetailsServiceImpl customUserDetails;
    private final PasswordEncoder encoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final FCMMapper fcmMapper;

    public boolean signUp(SignUpDTO signUpDTO) {
        // 두 유저가 같은 이메일로 동시에 회원가입한다면 오류가 날 수 있기 때문에
        // 중복확인을 한 번 더 수행해야함.
        boolean isUniqueEmail = userMapper.emailCheck(signUpDTO.getEmail()) == 0;
        boolean isUniqueNickName = userMapper.nickNameCheck(signUpDTO.getNickName()) == 0;
        if (isUniqueEmail && isUniqueNickName) {
            signUpDTO.setPassword(encoder.encode(signUpDTO.getPassword()));
            userMapper.insertUser(signUpDTO);
            return true;
        }
        return false;
    }

    public Map<String, String> signIn(SignInDTO signInDTO) {
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                signInDTO.getEmail(),
                signInDTO.getPassword()
        );
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        if (checkDuplicatedSignIn(signInDTO)) {
            refreshTokenMapper.deleteToken(signInDTO.getEmail());
            fcmMapper.deleteTokenByEmail(signInDTO.getEmail());
        }
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
        refreshTokenMapper.insertToken(refreshToken, signInDTO.getEmail());
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", accessToken);
        tokenMap.put("refreshToken", refreshToken);
        return tokenMap;
    }

    public boolean signOut(String userEmail) {
        int result = refreshTokenMapper.deleteToken(userEmail);
        return result > 0;
    }

    private boolean checkDuplicatedSignIn(SignInDTO signInDTO) {
        boolean isRefreshTokenAlreadyExist = refreshTokenMapper.countTokenByEmail(signInDTO.getEmail());
        boolean isFCMTokenAlreadyExist = fcmMapper.countTokenByEmail(signInDTO.getEmail());
        return (isRefreshTokenAlreadyExist && isFCMTokenAlreadyExist);
    }

    public boolean isValidEmail(String email) {
        int count = userMapper.emailCheck(email);
        return count == 0;
    }

    public boolean isValidNickName(String nickName) {
        int count = userMapper.nickNameCheck(nickName);
        return count == 0;
    }
}
