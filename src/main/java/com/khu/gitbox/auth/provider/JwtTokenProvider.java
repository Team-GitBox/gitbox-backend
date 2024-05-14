package com.khu.gitbox.auth.provider;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.khu.gitbox.auth.application.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {
	private static final String AUTHORITIES_CLAIM_NAME = "authority";

	@Value("${JWT_SECRET_KEY}")
	private String secretKey;

	@Value("${ACCESS_EXPIRY_SECONDS}")
	private int expirySeconds;

	public String createAccessToken(Authentication authentication) {
		final UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();

		final Instant now = Instant.now();
		final Instant expirationTime = now.plusSeconds(expirySeconds);

		final String authority = userDetails.getAuthority();

		return Jwts.builder()
			.claim("id", userDetails.getId())
			.subject((userDetails.getUsername()))
			.claim(AUTHORITIES_CLAIM_NAME, authority)
			.issuedAt(Date.from(now))
			.expiration(Date.from(expirationTime))
			.signWith(key())
			.compact();
	}

	public Authentication getAuthentication(String accessToken) {
		Claims claims = Jwts.parser()
			.verifyWith(key())
			.build()
			.parseSignedClaims(accessToken)
			.getPayload();

		Collection<? extends GrantedAuthority> authorities = List.of(
			new SimpleGrantedAuthority(claims.get(AUTHORITIES_CLAIM_NAME).toString()));

		UserDetails principal = new UserDetailsImpl(
			claims.get("id", Long.class),
			claims.getSubject(),
			null,
			authorities
		);
		return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
	}

	public void validateAccessToken(String accessToken) {
		Jwts.parser().verifyWith(key()).build().parse(accessToken);
	}

	public String createRefreshToken() {
		return UUID.randomUUID().toString();
	}

	private SecretKey key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}

	public String getEmail(String token) {
		Claims claims = Jwts.parser()
			.verifyWith(key()).build()
			.parseSignedClaims(token)
			.getPayload();

		return claims.getSubject();
	}

	public Long getId(String token) {
		Claims claims = Jwts.parser()
			.verifyWith(key()).build()
			.parseSignedClaims(token)
			.getPayload();

		return claims.get("id", Long.class);
	}
}

