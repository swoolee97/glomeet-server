package com.example.glomeet.config;

import com.example.glomeet.auth.ExceptionHandlerFilter;
import com.example.glomeet.auth.JwtAuthenticationFilter;
import com.example.glomeet.auth.JwtTokenProvider;
import com.example.glomeet.exception.AccessDeniedHandlerImpl;
import com.example.glomeet.security.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((auth) -> {
                            auth.requestMatchers("/auth/test").hasAuthority("USER");
                            // permitAll()은 필터를 거치지 않는다는 것이 아님.
                            // securityContext에 인증 정보가 없어도 통과된다는 것.
                            auth.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();
                            auth.requestMatchers(HttpMethod.POST, "/fcm/test").permitAll();
                            auth.requestMatchers(HttpMethod.POST, "/mail/auth").permitAll();
                            auth.requestMatchers(HttpMethod.POST, "/token/re-issue").permitAll();
                            auth.requestMatchers("/chat").permitAll();
                            auth.anyRequest().authenticated();
                        }
                ).addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(),
                        JwtAuthenticationFilter.class);
        http.exceptionHandling(manager -> manager.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new AccessDeniedHandlerImpl()));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
