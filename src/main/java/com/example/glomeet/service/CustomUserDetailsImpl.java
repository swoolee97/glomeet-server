package com.example.glomeet.service;

import com.example.glomeet.dto.UserDTO;
import com.example.glomeet.mapper.UserMapper;
import java.util.Collection;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsImpl implements UserDetailsService {
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserDTO userDTO = userMapper.findUserByEmail(userName);
        if (userDTO == null) {
            throw new UsernameNotFoundException("존재하지 않는 사용자입니다");
        }
        return new User(userDTO.getEmail(), userDTO.getPassword(), getAuthorities(userDTO));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserDTO userDTO) {
        // 권한 정보를 GrantedAuthority 객체의 컬렉션으로 변환합니다.
        return Collections.singletonList(new SimpleGrantedAuthority(userDTO.getRole()));
    }

}
