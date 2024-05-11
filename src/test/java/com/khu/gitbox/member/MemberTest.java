// package com.khu.gitbox.member;
//
// import com.khu.gitbox.domain.member.application.MemberService;
// import com.khu.gitbox.domain.member.entity.Member;
// import com.khu.gitbox.domain.member.infrastructure.MemberRepository;
// import com.khu.gitbox.domain.member.presentation.dto.MemberDto;
// import com.khu.gitbox.domain.member.presentation.dto.SignUpRequest;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
//
// @SpringBootTest
// public class MemberTest {
//
//     @Autowired
//     private MemberService memberService;
//
//
//
//     @Test
//     public void memberEditTest() {
//
//         SignUpRequest request = new SignUpRequest("emil@adfdas", "12314", "thdtjdgns");
//
//         memberService.signUp(request);
//
//         MemberDto memberDto = new MemberDto("emil@adfdas", "12314", "송성훈", null);
//
//         memberService.editMember(memberDto, 2L);
//
//         MemberDto memberDto1 = memberService.infoMember("emil@adfdas");
//
//         System.out.println("memberDto1 = " + memberDto1);
//     }
// }
