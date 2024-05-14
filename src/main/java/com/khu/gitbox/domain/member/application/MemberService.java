package com.khu.gitbox.domain.member.application;

import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.domain.member.entity.Member;
import com.khu.gitbox.domain.member.infrastructure.MemberRepository;
import com.khu.gitbox.domain.member.presentation.dto.MemberDto;
import com.khu.gitbox.domain.member.presentation.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long signUp(SignUpRequest request) {
        memberRepository.findByEmail(request.email()).ifPresent(member -> {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다. :" + member.getEmail());
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
            throw new CustomException(HttpStatus.NOT_FOUND, "현재 회원이 존재하지 않습니다.");
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
            throw new CustomException(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다.");
        });

        if (member.getId().equals(id)) {
            member.updateMember(memberDto.getEmail(), memberDto.getPassword(), memberDto.getName(),
                    memberDto.getProfileImage());
        } else {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "현재 토큰정보가 유효하지 않습니다.");
        }

        return id;
    }

    public void deleteMember(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> {
            throw new CustomException(HttpStatus.NOT_FOUND, "삭제하고자 하는 회원을 찾을 수 없습니다.");
        });

        memberRepository.deleteById(member.getId());

    }
}
