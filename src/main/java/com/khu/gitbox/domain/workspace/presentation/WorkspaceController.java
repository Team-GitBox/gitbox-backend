package com.khu.gitbox.domain.workspace.presentation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.khu.gitbox.common.response.ApiResponse;
import com.khu.gitbox.domain.action.ActionHistoryDto;
import com.khu.gitbox.domain.action.ActionHistoryService;
import com.khu.gitbox.domain.workspace.application.WorkspaceService;
import com.khu.gitbox.domain.workspace.presentation.dto.AddMembers;
import com.khu.gitbox.domain.workspace.presentation.dto.CreateWorkspace;
import com.khu.gitbox.domain.workspace.presentation.dto.DeleteMembers;
import com.khu.gitbox.domain.workspace.presentation.dto.WorkspaceDetail;
import com.khu.gitbox.util.SecurityContextUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/workspace")
@RequiredArgsConstructor
public class WorkspaceController {
	private final WorkspaceService workspaceService;
	private final ActionHistoryService actionHistoryService;

	// 워크스페이스 생성
	@PostMapping
	public ApiResponse<Long> createWorkspace(@Valid @RequestBody CreateWorkspace workspace) {
		Long ownerId = SecurityContextUtil.getCurrentMemberId();
		Long workspaceId = workspaceService.createWorkspace(workspace, ownerId);
		return ApiResponse.created(workspaceId);
	}

	//워크스페이스 정보 가져오기
	@GetMapping("/{workspaceId}")
	public ApiResponse<WorkspaceDetail> getWorkspace(@PathVariable Long workspaceId) {
		Long memberId = SecurityContextUtil.getCurrentMemberId();
		WorkspaceDetail workspaceDetail = workspaceService.getWorkspaceDetail(workspaceId, memberId);
		return ApiResponse.created(workspaceDetail); // 워크스페이스 정보 반환
	}

	// 워크스페이스 멤버 추가
	@PostMapping("/{workspaceId}/members")
	public ApiResponse<List<Long>> addMembersToWorkspace(
		@PathVariable Long workspaceId,
		@Valid @RequestBody AddMembers addMembers) {
		Long requestOwnerId = SecurityContextUtil.getCurrentMemberId();
		List<Long> memberIds = workspaceService.addMembersToWorkspace(
			workspaceId,
			requestOwnerId,
			addMembers.getAddMemberEmail());
		return ApiResponse.created(memberIds);
	}

	//워크스페이스 멤버 삭제
	@DeleteMapping("/{workspaceId}/members")
	public ApiResponse<Void> deleteWorkspaceMembers(
		@PathVariable Long workspaceId,
		@Valid @RequestBody DeleteMembers deleteMembersEmails) {
		Long requestOwnerId = SecurityContextUtil.getCurrentMemberId();
		workspaceService.deleteMembers(workspaceId, requestOwnerId, deleteMembersEmails.getMemberEmails());
		return ApiResponse.ok();
	}

	//워크스페이스 삭제
	@DeleteMapping("/{workspaceId}")
	public ApiResponse<Void> deleteWorkspace(@PathVariable Long workspaceId) {
		Long memberId = SecurityContextUtil.getCurrentMemberId();
		workspaceService.deleteWorkspace(workspaceId, memberId);
		return ApiResponse.ok();
	}

	@GetMapping("/{workspace}/history")
	public ApiResponse<Page> history(
		@PathVariable Long workspaceId,
		@RequestParam int page,
		@PageableDefault(page = 0, size = 7) Pageable pageable) {

		Page<ActionHistoryDto> actionHistoryList = actionHistoryService.getActionHistoryList(page, pageable,
			workspaceId);

		return ApiResponse.ok(actionHistoryList);
	}
}

