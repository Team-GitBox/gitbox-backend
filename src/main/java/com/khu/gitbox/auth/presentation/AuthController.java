package com.khu.gitbox.auth.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khu.gitbox.auth.application.AuthService;
import com.khu.gitbox.auth.presentation.dto.AuthResponse;
import com.khu.gitbox.auth.presentation.dto.LoginRequest;
import com.khu.gitbox.common.response.ApiResponse;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request,
		HttpServletResponse response) {
		AuthResponse authResponse = authService.login(request);

		// 쿠키 생성
		Cookie cookie = new Cookie("accessToken", authResponse.accessToken());
		cookie.setHttpOnly(true); // 클라이언트의 스크립트에서 접근하지 못하도록 설정
		cookie.setPath("/"); // 쿠키를 전송할 요청 경로
		cookie.setSecure(true); // HTTPS를 통해서만 쿠키를 전송
		cookie.setMaxAge(7 * 24 * 60 * 60); // 쿠키의 만료 시간 (예: 일주일)

		// 응답에 쿠키 추가
		response.addCookie(cookie);

		return new ResponseEntity<>(ApiResponse.ok(authResponse), HttpStatus.OK);
	}
}
