package com.khu.gitbox.domain.member.application;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.domain.member.entity.Member;
import com.khu.gitbox.domain.member.infrastructure.MemberRepository;
import com.khu.gitbox.domain.member.presentation.dto.SignUpRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	public Long signUp(SignUpRequest request) {
		memberRepository.findByEmail(request.email()).ifPresent(member -> {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Member already exists :" + member.getEmail());
		});

		Member member = Member.builder()
			.email(request.email())
			.name(request.name())
			.password(passwordEncoder.encode(request.password()))
			.build();

		return memberRepository.save(member).getId();
	}
}
