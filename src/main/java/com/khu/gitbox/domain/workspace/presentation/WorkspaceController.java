package com.khu.gitbox.domain.workspace.presentation;

import com.khu.gitbox.auth.provider.JwtTokenProvider;
import com.khu.gitbox.common.response.ApiResponse;
import com.khu.gitbox.domain.member.infrastructure.MemberRepository;
import com.khu.gitbox.domain.workspace.application.WorkspaceServiceImpl;
import com.khu.gitbox.domain.workspace.entity.Workspace;
import com.khu.gitbox.domain.workspace.presentation.dto.AddMembers;
import com.khu.gitbox.domain.workspace.presentation.dto.DeleteMembers;
import com.khu.gitbox.domain.workspace.presentation.dto.MakeWorkspace;
import com.khu.gitbox.domain.workspace.presentation.dto.WorkspaceDetail;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workspace")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceServiceImpl workspaceService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @PostMapping("")
    public ResponseEntity<ApiResponse<Long>> createWorkspace(@Valid @RequestBody MakeWorkspace workspace, @RequestHeader("Cookie") String cookie) {
        String token = cookie.substring(12);
        Long ownerId = jwtTokenProvider.getId(token);

        // 모든 이메일이 존재하면 워크스페이스 생성 진행
        Long id = workspaceService.createWorkspace(workspace, ownerId);
        return ResponseEntity.ok(ApiResponse.created(id));
    }

    @PostMapping("/members")
    public ResponseEntity<ApiResponse<Long>> addMembers(@Valid @RequestBody AddMembers addMembers, @RequestHeader("Cookie") String cookie) {
        String token = cookie.substring(12);
        Long memberId = jwtTokenProvider.getId(token);

        Workspace workspace = workspaceService.findById(addMembers.getWorkspaceId());

        if (workspace.getOwnerId().equals(memberId)) {
            workspaceService.addMembers(addMembers.getAddMemberEmail(), addMembers.getWorkspaceId());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceDetail> getWorkspace(@PathVariable Long workspaceId, @RequestHeader("Cookie") String cookie) {
        String token = cookie.substring(12); // 토큰 추출
        Long memberId = jwtTokenProvider.getId(token); // JWT로부터 멤버 ID 추출

        WorkspaceDetail workspaceDetail = workspaceService.findByMemberIdAndWorkspaceId(workspaceId, memberId); // 서비스 호출

        return ResponseEntity.ok(workspaceDetail); // 워크스페이스 정보 반환
    }


    @DeleteMapping("/member")
    public ResponseEntity<?> deleteWorkspaceMembers(@Valid @RequestBody DeleteMembers deleteMembers, @RequestHeader("Cookie") String cookie) {
        String token = cookie.substring(12);
        Long memberId = jwtTokenProvider.getId(token);

        // 요청에서 workspaceId를 직접 사용합니다.
        Workspace workspace = workspaceService.findById(deleteMembers.getWorkspaceId());

        if (workspace.getOwnerId().equals(memberId)) {
            // deleteMemberIds 리스트를 사용하여 멤버들을 삭제합니다.
            workspaceService.deleteMembers(deleteMembers.getDeleteMemberIds());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }

    @DeleteMapping("/{workspaceId}") // 삭제
    public ResponseEntity<?> deleteWorkspace(@PathVariable Long workspaceId, @RequestHeader("Cookie") String cookie) {
        String token = cookie.substring(12);
        Long memberId = jwtTokenProvider.getId(token);

        // 워크스페이스 이름을 사용하여 워크스페이스 정보 조회
        Workspace workspace = workspaceService.findById(workspaceId);

        // 워크스페이스의 ownerId와 memberId 비교
        if (workspace.getOwnerId().equals(memberId)) {
            // 일치할 경우, 워크스페이스 삭제 로직 실행
            workspaceService.deleteWorkspaces(workspace.getId());
            return ResponseEntity.ok().build(); // 삭제 성공 응답
        } else {
            // 소유자가 일치하지 않을 경우, 에러 응답
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied"); // 접근 거부 응답
        }
    }


}
