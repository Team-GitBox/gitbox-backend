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

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long signUp(SignUpRequest request) {
        memberRepository.findByEmail(request.email()).ifPresent(member -> {
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다 : " + member.getEmail());
        });

        Member member = Member.builder()
                .email(request.email())
                .name(request.name())
                .password(passwordEncoder.encode(request.password()))
                .build();

        return memberRepository.save(member).getId();
    }

    // 회원 정보 조회하기 (1사람)
    public MemberDto getMemberDetail(Long memberId) {
        Member member = findMemberById(memberId);

        return MemberDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .password(member.getPassword())
                .profileImage(member.getProfileImage())
                .build();
    }

    public Long editMember(MemberDto memberDto, Long memberId) {
        Member member = findMemberById(memberId);
        member.updateMember(
                memberDto.getEmail(),
                passwordEncoder.encode(memberDto.getPassword()),
                memberDto.getName(),
                memberDto.getProfileImage());

        return memberId;
    }

    public void deleteMember(Long memberId) {
        Member member = findMemberById(memberId);
        memberRepository.deleteById(member.getId());
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다 : " + memberId));
    }
}
