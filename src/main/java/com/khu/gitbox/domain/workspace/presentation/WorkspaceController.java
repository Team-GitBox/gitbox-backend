package com.khu.gitbox.domain.workspace.presentation;

import com.khu.gitbox.auth.provider.JwtTokenProvider;
import com.khu.gitbox.common.response.ApiResponse;
import com.khu.gitbox.domain.action.ActionHistoryDto;
import com.khu.gitbox.domain.action.ActionHistoryService;
import com.khu.gitbox.domain.workspace.application.WorkspaceServiceImpl;
import com.khu.gitbox.domain.workspace.entity.Workspace;
import com.khu.gitbox.domain.workspace.presentation.dto.AddMembers;
import com.khu.gitbox.domain.workspace.presentation.dto.CreateWorkspace;
import com.khu.gitbox.domain.workspace.presentation.dto.DeleteMembers;
import com.khu.gitbox.domain.workspace.presentation.dto.WorkspaceDetail;
import com.khu.gitbox.util.SecurityContextUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workspace")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceServiceImpl workspaceService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ActionHistoryService actionHistoryService;

    // 워크스페이스 생성
    @PostMapping("")
    public ResponseEntity<ApiResponse<Long>> createWorkspace(@Valid @RequestBody CreateWorkspace workspace,
                                                             @RequestHeader(value = "Cookie", required = false) String cookie) {

        Long ownerId = SecurityContextUtil.getCurrentMemberId();
        Long id = workspaceService.createWorkspace(workspace, ownerId);

        return ResponseEntity.ok(ApiResponse.created(id));
    }

    // 워크스페이스 멤버 추가
    @PostMapping("/{workspaceId}/members")
    public ResponseEntity<String> addMembers(@PathVariable Long workspaceId, @Valid @RequestBody AddMembers addMembers,
                                             @RequestHeader("Cookie") String cookie) {

        Long memberId = SecurityContextUtil.getCurrentMemberId();
        workspaceService.addMembers(addMembers.getAddMemberEmail(), workspaceId, memberId);

        return ResponseEntity.ok().build();
    }

    //워크스페이스 정보 가져오기
    @GetMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceDetail> getWorkspace(@PathVariable Long workspaceId,
                                                        @RequestHeader("Cookie") String cookie) {

        Long memberId = SecurityContextUtil.getCurrentMemberId();
        WorkspaceDetail workspaceDetail = workspaceService.findByMemberIdAndWorkspaceId(workspaceId,
                memberId); // 서비스 호출

        return ResponseEntity.ok(workspaceDetail); // 워크스페이스 정보 반환
    }

    //워크스페이스 멤버 삭제 문제 있는듯 ?
    @DeleteMapping("/{workspaceId}/members")
    public ResponseEntity<?> deleteWorkspaceMembers(@PathVariable Long workspaceId,
                                                    @Valid @RequestBody DeleteMembers deleteMembers, @RequestHeader("Cookie") String cookie) {

        Long requestMemberId = SecurityContextUtil.getCurrentMemberId();
        Workspace workspace = workspaceService.findById(workspaceId);

        workspaceService.deleteMembers(deleteMembers.getDeleteMemberIds(), requestMemberId);
        return ResponseEntity.ok().build();
    }

    //워크스페이스 삭제
    @DeleteMapping("/{workspaceId}") // 삭제
    public ResponseEntity<?> deleteWorkspace(@PathVariable Long workspaceId, @RequestHeader("Cookie") String cookie) {

        Long memberId = SecurityContextUtil.getCurrentMemberId();
        Workspace workspace = workspaceService.findById(workspaceId);

        workspaceService.deleteWorkspaces(workspace.getId(), memberId);
        return ResponseEntity.ok().build();
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

