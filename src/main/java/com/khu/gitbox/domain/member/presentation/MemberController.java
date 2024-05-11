package com.khu.gitbox.domain.member.presentation;

import com.khu.gitbox.auth.provider.JwtTokenProvider;
import com.khu.gitbox.common.response.ApiResponse;
import com.khu.gitbox.domain.member.application.MemberService;
import com.khu.gitbox.domain.member.presentation.dto.MemberDto;
import com.khu.gitbox.domain.member.presentation.dto.SignUpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse<MemberDto>> myInfo(@RequestHeader("Cookie") String cookie) {

        String email = jwtTokenProvider.getEmail(cookie.substring(12));

        MemberDto memberDto = memberService.infoMember(email);

        return ResponseEntity.ok(ApiResponse.ok(memberDto));
    }

    @PostMapping("/my/info")
    public ResponseEntity<ApiResponse<Long>> editMember(@RequestBody MemberDto memberDto, @RequestHeader("Cookie") String cookie) {

        System.out.println("memberDto = " + memberDto);
        Long id = jwtTokenProvider.getId(cookie.substring(12));

        System.out.println("id = " + id);

        Long responseId = memberService.editMember(memberDto, id);

        return ResponseEntity.ok(ApiResponse.ok(responseId));
    }

    @DeleteMapping("/my/info")
    public ResponseEntity<ApiResponse<String>> deleteMember(@RequestHeader("Cookie") String cookie) {

        String email = jwtTokenProvider.getEmail(cookie.substring(12));

        memberService.deleteMember(email);

        return ResponseEntity.ok(ApiResponse.delete(email));
    }
}
