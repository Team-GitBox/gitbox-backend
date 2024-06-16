package com.khu.gitbox.domain.member.presentation;

import com.khu.gitbox.common.response.ApiResponse;
import com.khu.gitbox.domain.member.application.MemberService;
import com.khu.gitbox.domain.member.presentation.dto.MemberDto;
import com.khu.gitbox.domain.member.presentation.dto.SignUpRequest;
import com.khu.gitbox.util.SecurityContextUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입")
    @PostMapping("/register")
    public ApiResponse<Long> signUp(@Valid @RequestBody SignUpRequest request) {
        Long id = memberService.signUp(request);
        return ApiResponse.created(id);
    }

    @Operation(summary = "내 정보 조회")
    @GetMapping(value = {"/my/info"})
    public ApiResponse<MemberDto> myInfo() {
        Long currentMemberId = SecurityContextUtil.getCurrentMemberId();
        MemberDto memberDto = memberService.getMemberDetail(currentMemberId);
        return ApiResponse.ok(memberDto);
    }

    @Operation(summary = "내 정보 수정")
    @PatchMapping("/my/info")
    public ApiResponse<Long> editMember(@RequestBody MemberDto memberDto) {
        final Long currentMemberId = SecurityContextUtil.getCurrentMemberId();
        Long responseId = memberService.editMember(memberDto, currentMemberId);
        return ApiResponse.ok(responseId);
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/my/info")
    public ApiResponse<Void> deleteMember() {
        final Long currentMemberId = SecurityContextUtil.getCurrentMemberId();
        memberService.deleteMember(currentMemberId);
        return ApiResponse.delete(null);
    }
}
