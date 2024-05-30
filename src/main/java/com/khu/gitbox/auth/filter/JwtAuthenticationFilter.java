package com.khu.gitbox.auth.filter;

import static org.springframework.util.StringUtils.*;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.khu.gitbox.auth.provider.JwtTokenProvider;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final String AUTHENTICATION_SCHEME = "Bearer ";
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		try {
			String accessToken = parseJwt(request);
			jwtTokenProvider.validateAccessToken(accessToken);
			Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (ExpiredJwtException ex) {
			logger.warn("ExpiredJwtException Occurred : ", ex);
			throw new CredentialsExpiredException("토큰의 유효기간이 만료되었습니다.", ex);
		} catch (Exception ex) {
			logger.warn("JwtAuthentication Failed. : ", ex);
			throw new BadCredentialsException("토큰 인증에 실패하였습니다.");
		}

		filterChain.doFilter(request, response);
	}

	private String parseJwt(HttpServletRequest request) {
		// Cookie[] cookies = request.getCookies();
		// if (cookies != null) {
		// 	for (Cookie cookie : cookies) {
		// 		if ("accessToken".equals(cookie.getName())) {
		// 			log.info("cookies : {}", cookie.getValue());
		// 			return cookie.getValue();
		// 		}
		// 	}
		// }

		String bearerToken = request.getHeader("Authorization");
		if (hasText(bearerToken) && bearerToken.startsWith(AUTHENTICATION_SCHEME)) {
			return bearerToken.substring(AUTHENTICATION_SCHEME.length());
		}
		throw new AuthenticationCredentialsNotFoundException("토큰이 존재하지 않습니다.");
	}
}
