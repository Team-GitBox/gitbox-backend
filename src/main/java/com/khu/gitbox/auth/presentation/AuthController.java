package com.khu.gitbox.auth.presentation;

import com.khu.gitbox.auth.application.AuthService;
import com.khu.gitbox.auth.presentation.dto.AuthResponse;
import com.khu.gitbox.auth.presentation.dto.LoginRequest;
import com.khu.gitbox.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);

        // 쿠키 생성
        return ResponseEntity.ok()
                .header("Set-Cookie", getAccessTokenHeader(authResponse.accessToken()))
                .body(ApiResponse.ok(authResponse));
    }

    private String getAccessTokenHeader(String accessToken) {
        return "accessToken=" + accessToken + "; Path=/; HttpOnly; SameSite=None; Secure";
    }
}
