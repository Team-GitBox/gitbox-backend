package com.khu.gitbox.domain.workspace.presentation;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khu.gitbox.common.response.ApiResponse;
import com.khu.gitbox.domain.action.ActionHistoryService;
import com.khu.gitbox.domain.workspace.application.WorkspaceService;
import com.khu.gitbox.domain.workspace.presentation.dto.ActionHistoryGetResponse;
import com.khu.gitbox.domain.workspace.presentation.dto.AddMembers;
import com.khu.gitbox.domain.workspace.presentation.dto.CreateWorkspace;
import com.khu.gitbox.domain.workspace.presentation.dto.DeleteMembers;
import com.khu.gitbox.domain.workspace.presentation.dto.WorkspaceDetail;
import com.khu.gitbox.domain.workspace.presentation.dto.WorkspaceSummary;
import com.khu.gitbox.util.SecurityContextUtil;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/workspace")
@RequiredArgsConstructor
public class WorkspaceController {
	private final WorkspaceService workspaceService;
	private final ActionHistoryService actionHistoryService;

	@Operation(summary = "워크스페이스 생성")
	@PostMapping
	public ApiResponse<Long> createWorkspace(@Valid @RequestBody CreateWorkspace workspace) {
		Long ownerId = SecurityContextUtil.getCurrentMemberId();
		Long workspaceId = workspaceService.createWorkspace(workspace, ownerId);
		return ApiResponse.created(workspaceId);
	}

	@Operation(summary = "내 워크스페이스 목록 조회")
	@GetMapping
	public ApiResponse<List<WorkspaceSummary>> getMyWorkspace() {
		Long memberId = SecurityContextUtil.getCurrentMemberId();
		List<WorkspaceSummary> response = workspaceService.getWorkspaces(memberId);
		return ApiResponse.ok(response);
	}

	@Operation(summary = "워크스페이스 상세 조회")
	@GetMapping("/{workspaceId}")
	public ApiResponse<WorkspaceDetail> getWorkspace(@PathVariable Long workspaceId) {
		Long memberId = SecurityContextUtil.getCurrentMemberId();
		WorkspaceDetail workspaceDetail = workspaceService.getWorkspaceDetail(workspaceId, memberId);
		return ApiResponse.created(workspaceDetail); // 워크스페이스 정보 반환
	}

	@Operation(summary = "워크스페이스 멤버 추가")
	@PostMapping("/{workspaceId}/members")
	public ApiResponse<List<Long>> addMembersToWorkspace(
		@PathVariable Long workspaceId,
		@Valid @RequestBody AddMembers addMembers) {
		Long requestOwnerId = SecurityContextUtil.getCurrentMemberId();
		List<Long> memberIds = workspaceService.addMembersToWorkspace(
			workspaceId,
			requestOwnerId,
			addMembers.getMemberEmails());
		return ApiResponse.created(memberIds);
	}

	@Operation(summary = "워크스페이스 멤버 삭제")
	@DeleteMapping("/{workspaceId}/members")
	public ApiResponse<Void> deleteWorkspaceMembers(
		@PathVariable Long workspaceId,
		@Valid @RequestBody DeleteMembers deleteMembersEmails) {
		Long requestOwnerId = SecurityContextUtil.getCurrentMemberId();
		workspaceService.deleteMembers(workspaceId, requestOwnerId, deleteMembersEmails.getMemberEmails());
		return ApiResponse.ok();
	}

	@Operation(summary = "워크스페이스 삭제")
	@DeleteMapping("/{workspaceId}")
	public ApiResponse<Void> deleteWorkspace(@PathVariable Long workspaceId) {
		Long memberId = SecurityContextUtil.getCurrentMemberId();
		workspaceService.deleteWorkspace(workspaceId, memberId);
		return ApiResponse.ok();
	}

	@Operation(summary = "워크스페이스 히스토리 조회")
	@GetMapping("/{workspaceId}/history")
	public ApiResponse<List<ActionHistoryGetResponse>> history(
		@PathVariable Long workspaceId) {
		List<ActionHistoryGetResponse> actionHistoryList = actionHistoryService.getActionHistories(workspaceId);
		return ApiResponse.ok(actionHistoryList);
	}
}

