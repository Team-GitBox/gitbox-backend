package com.khu.gitbox.domain.member.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khu.gitbox.auth.provider.JwtTokenProvider;
import com.khu.gitbox.common.response.ApiResponse;
import com.khu.gitbox.domain.member.application.MemberService;
import com.khu.gitbox.domain.member.presentation.dto.MemberDto;
import com.khu.gitbox.domain.member.presentation.dto.SignUpRequest;
import com.khu.gitbox.util.SecurityContextUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final JwtTokenProvider jwtTokenProvider;

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<Long>> signUp(@Valid @RequestBody SignUpRequest request) {
		Long id = memberService.signUp(request);
		return ResponseEntity.ok(ApiResponse.created(id));
	}

	@GetMapping(value = {"/my", "/my/info"})
	public ResponseEntity<ApiResponse<MemberDto>> myInfo() {
		Long currentMemberId = SecurityContextUtil.getCurrentMemberId();
		MemberDto memberDto = memberService.getMemberDetail(currentMemberId);
		return ResponseEntity.ok(ApiResponse.ok(memberDto));
	}

	@PatchMapping("/my/info")
	public ResponseEntity<ApiResponse<Long>> editMember(@RequestBody MemberDto memberDto) {
		final Long currentMemberId = SecurityContextUtil.getCurrentMemberId();
		Long responseId = memberService.editMember(memberDto, currentMemberId);
		return ResponseEntity.ok(ApiResponse.ok(responseId));
	}

	@DeleteMapping("/my/info")
	public ResponseEntity<ApiResponse<Void>> deleteMember() {
		final Long currentMemberId = SecurityContextUtil.getCurrentMemberId();
		memberService.deleteMember(currentMemberId);
		return ResponseEntity.ok(ApiResponse.delete(null));
	}
}
