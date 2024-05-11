package com.khu.gitbox.auth.application;

import com.khu.gitbox.auth.presentation.dto.AuthResponse;
import com.khu.gitbox.auth.presentation.dto.LoginRequest;
import com.khu.gitbox.auth.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDetailsService memberDetailsService;

    public AuthResponse login(LoginRequest request) {
        // 인증 전의 auth 객체
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        );
		
        // 인증 및 Authentication 객체 생성
        Authentication authenticated = authenticationManager.authenticate(authentication);

        // 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(authenticated);
        // String refreshToken = jwtTokenProvider.generateRefreshToken();

        return new AuthResponse(accessToken, "refreshToken");
    }
}
