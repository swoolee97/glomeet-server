package com.example.glomeet.service;

import com.example.glomeet.auth.JwtTokenProvider;
import com.example.glomeet.dto.TokenDTO;
import com.example.glomeet.entity.RefreshToken;
import com.example.glomeet.repository.RefreshTokenRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenDTO reIssueAccessToken(String email, String refreshToken) throws BadRequestException {
        if (!isSameToken(email, refreshToken)) {
            throw new BadRequestException("DB에 있는 리프레시 토큰과 다름. 재로그인 바람");
        }
        jwtTokenProvider.validateToken(refreshToken);
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String reIssuedRefreshToken = jwtTokenProvider.generateRefreshToken(authentication);
        refreshTokenRepository.save(new RefreshToken(email, reIssuedRefreshToken));
        TokenDTO tokenDTO = new TokenDTO(accessToken, reIssuedRefreshToken);
        return tokenDTO;
    }

    private boolean isSameToken(String email, String refreshToken) {
        Optional<RefreshToken> tokenInDatabase = refreshTokenRepository.findByEmail(email);
        if (tokenInDatabase.isEmpty()) {
            return false;
        }
        return tokenInDatabase.get().getRefreshToken().equals(refreshToken);
    }

}
