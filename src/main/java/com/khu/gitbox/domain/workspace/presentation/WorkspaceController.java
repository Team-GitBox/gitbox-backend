package com.khu.gitbox.domain.workspace.presentation;

import com.khu.gitbox.auth.provider.JwtTokenProvider;
import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.common.response.ApiResponse;
import com.khu.gitbox.domain.member.entity.Member;
import com.khu.gitbox.domain.member.infrastructure.MemberRepository;
import com.khu.gitbox.domain.workspace.application.WorkspaceServiceImpl;
import com.khu.gitbox.domain.workspace.entity.Workspace;
import com.khu.gitbox.domain.workspace.entity.WorkspaceMember;
import com.khu.gitbox.domain.workspace.presentation.dto.MakeWorkspace;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
		Long id = workspaceService.save(workspace, ownerId);
		return ResponseEntity.ok(ApiResponse.created(id));
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
			workspaceService.delete(workspace.getId());
			return ResponseEntity.ok().build(); // 삭제 성공 응답
		} else {
			// 소유자가 일치하지 않을 경우, 에러 응답
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied"); // 접근 거부 응답
		}
	}
}
