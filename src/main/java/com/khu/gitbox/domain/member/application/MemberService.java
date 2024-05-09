package com.khu.gitbox.domain.member.application;

import com.khu.gitbox.domain.member.presentation.dto.MemberDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.domain.member.entity.Member;
import com.khu.gitbox.domain.member.infrastructure.MemberRepository;
import com.khu.gitbox.domain.member.presentation.dto.SignUpRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Optional;

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

	// 회원 정보 조회하기 (1사람)
	public MemberDto infoMember(String email) {
		Optional<Member> member = memberRepository.findByEmail(email);

		if(!member.isPresent()) {
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

	@Transactional
	public Long editMember(MemberDto memberDto, Long id) {
		Optional<Member> member = memberRepository.findById(id);

		System.out.println("member = " + member.get().getId());
		System.out.println("member = " + member.get().getName());

		System.out.println("memberDto = " + memberDto.getName());
		System.out.println("id = " + id);

		if(member.isEmpty())
			throw new CustomException(HttpStatus.NOT_FOUND, "Member is not exist");

		if(member.get().getId().equals(id)) {

			member.get().builder()
					.email(memberDto.getEmail())
					.name(memberDto.getName())
					.password(memberDto.getPassword())
					.profileImage(memberDto.getProfileImage())
					.build();

			Member save = memberRepository.save(member.get());
		}
		else {
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "token id is not match the input value.");
        }

		return id;
	}

	public void deleteMember(String email) {
		Optional<Member> member = memberRepository.findByEmail(email);

		if(member.isPresent()) {
			memberRepository.deleteById(member.get().getId());

			return;
		}

		throw new CustomException(HttpStatus.NOT_FOUND, "Member is not exist");
	}
}
