package com.khu.gitbox.domain.pullRequest.presentation;

import com.khu.gitbox.common.response.ApiResponse;
import com.khu.gitbox.domain.pullRequest.application.PullRequestService;
import com.khu.gitbox.domain.pullRequest.presentation.dto.PullRequestCommentCreateRequest;
import com.khu.gitbox.domain.pullRequest.presentation.dto.PullRequestGetResponse;
import com.khu.gitbox.util.SecurityContextUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pull-request")
public class PullRequestController {
    private final PullRequestService pullRequestService;

    @Operation(summary = "PR 상세 정보 조회")
    @GetMapping("/{pullRequestId}")
    public ApiResponse<PullRequestGetResponse> getPullRequest(@PathVariable Long pullRequestId) {
        PullRequestGetResponse pullRequestGetResponse = pullRequestService.getPullRequest(pullRequestId);
        return ApiResponse.ok(pullRequestGetResponse);
    }

    @Operation(summary = "PR 코멘트 생성 (PR 승인/거절)")
    @PostMapping("/{pullRequestId}/comments")
    public ApiResponse<Boolean> createPullRequestComment(
            @RequestBody PullRequestCommentCreateRequest commentCreateRequest,
            @PathVariable Long pullRequestId) {
        Long reviewerId = SecurityContextUtil.getCurrentMemberId();
        pullRequestService.createComment(commentCreateRequest, reviewerId, pullRequestId);

        return ApiResponse.created(commentCreateRequest.getIsApproved());
    }

}
