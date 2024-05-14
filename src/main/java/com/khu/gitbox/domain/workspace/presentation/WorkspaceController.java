package com.khu.gitbox.domain.workspace.presentation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.khu.gitbox.auth.provider.JwtTokenProvider;
import com.khu.gitbox.common.response.ApiResponse;
import com.khu.gitbox.domain.action.ActionHistoryDto;
import com.khu.gitbox.domain.action.ActionHistoryService;
import com.khu.gitbox.domain.workspace.application.WorkspaceServiceImpl;
import com.khu.gitbox.domain.workspace.entity.Workspace;
import com.khu.gitbox.domain.workspace.presentation.dto.AddMembers;
import com.khu.gitbox.domain.workspace.presentation.dto.DeleteMembers;
import com.khu.gitbox.domain.workspace.presentation.dto.MakeWorkspace;
import com.khu.gitbox.domain.workspace.presentation.dto.WorkspaceDetail;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/workspace")
@RequiredArgsConstructor
public class WorkspaceController {

	private final WorkspaceServiceImpl workspaceService;
	private final JwtTokenProvider jwtTokenProvider;
	private final ActionHistoryService actionHistoryService;

	// TODO: ownerId 필요없음, Cookie에서 가져올 필요 없음
	// 워크스페이스 생성
	@PostMapping("")
	public ResponseEntity<ApiResponse<Long>> createWorkspace(@Valid @RequestBody MakeWorkspace workspace,
		@RequestHeader(value = "Cookie", required = false) String cookie) {
		String token = cookie.substring(12);
		Long ownerId = jwtTokenProvider.getId(token);

		// 모든 이메일이 존재하면 워크스페이스 생성 진행
		Long id = workspaceService.createWorkspace(workspace, ownerId);
		return ResponseEntity.ok(ApiResponse.created(id));
	}

	// 워크스페이스 멤버 추가
	@PostMapping("/{workspaceId}/members")
	public ResponseEntity<String> addMembers(@PathVariable Long workspaceId, @Valid @RequestBody AddMembers addMembers,
		@RequestHeader("Cookie") String cookie) {
		String token = cookie.substring(12);
		Long memberId = jwtTokenProvider.getId(token);

		Workspace workspace = workspaceService.findById(workspaceId);

		if (workspace.getOwnerId().equals(memberId)) {
			workspaceService.addMembers(addMembers.getAddMemberEmail(), workspaceId);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당워크스페이스 사용자가 아닙니다.");
		}
	}

	//워크스페이스 정보 가져오기
	@GetMapping("/{workspaceId}")
	public ResponseEntity<WorkspaceDetail> getWorkspace(@PathVariable Long workspaceId,
		@RequestHeader("Cookie") String cookie) {
		String token = cookie.substring(12); // 토큰 추출
		Long memberId = jwtTokenProvider.getId(token); // JWT로부터 멤버 ID 추출

		WorkspaceDetail workspaceDetail = workspaceService.findByMemberIdAndWorkspaceId(workspaceId,
			memberId); // 서비스 호출

		return ResponseEntity.ok(workspaceDetail); // 워크스페이스 정보 반환
	}

	//워크스페이스 멤버 삭제
	@DeleteMapping("/{workspaceId}/members")
	public ResponseEntity<?> deleteWorkspaceMembers(@PathVariable Long workspaceId,
		@Valid @RequestBody DeleteMembers deleteMembers, @RequestHeader("Cookie") String cookie) {
		String token = cookie.substring(12);
		Long memberId = jwtTokenProvider.getId(token);

		// 요청에서 workspaceId를 직접 사용합니다.
		Workspace workspace = workspaceService.findById(workspaceId);

		if (workspace.getOwnerId().equals(memberId)) {
			// deleteMemberIds 리스트를 사용하여 멤버들을 삭제합니다.
			workspaceService.deleteMembers(deleteMembers.getDeleteMemberIds());
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("워크스페이스 owner가 아닙니다.");
		}
	}

	//워크스페이스 삭제
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

	@GetMapping("/{workspace}/history")
	public ResponseEntity<ApiResponse<Page>> history(
		@PathVariable Long workspaceId,
		@RequestParam int page,
		@PageableDefault(page = 0, size = 7) Pageable pageable) {

		Page<ActionHistoryDto> actionHistoryList = actionHistoryService.getActionHistoryList(page, pageable,
			workspaceId);

		return ResponseEntity.ok(ApiResponse.ok(actionHistoryList));

	}

}
