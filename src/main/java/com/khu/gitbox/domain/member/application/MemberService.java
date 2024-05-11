package com.khu.gitbox.domain.member.application;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.domain.member.entity.Member;
import com.khu.gitbox.domain.member.infrastructure.MemberRepository;
import com.khu.gitbox.domain.member.presentation.dto.MemberDto;
import com.khu.gitbox.domain.member.presentation.dto.SignUpRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
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

	// 회원 정보 조회하기 (1사람)
	public MemberDto infoMember(String email) {
		Optional<Member> member = memberRepository.findByEmail(email);

		if (!member.isPresent()) {
			throw new CustomException(HttpStatus.NOT_FOUND, "Member is not exist.");
		}

		Member mem = member.get();

		MemberDto memberDto = MemberDto.builder()
			.email(mem.getEmail())
			.name(mem.getName())
			.password(mem.getPassword())
			.profileImage(mem.getProfileImage())
			.build();

		return memberDto;
	}

	public Long editMember(MemberDto memberDto, Long id) {
		Member member = memberRepository.findById(id).orElseThrow(() -> {
			throw new CustomException(HttpStatus.NOT_FOUND, "Member is not exist");
		});

		if (member.getId().equals(id)) {
			member.updateMember(memberDto.getEmail(), memberDto.getPassword(), memberDto.getName(),
				memberDto.getProfileImage());
		} else {
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "token id is not match the input value.");
		}

		return id;
	}

	public void deleteMember(String email) {
		Member member = memberRepository.findByEmail(email).orElseThrow(() -> {
			throw new CustomException(HttpStatus.NOT_FOUND, "Member is not exist");
		});

		memberRepository.deleteById(member.getId());

	}
}
