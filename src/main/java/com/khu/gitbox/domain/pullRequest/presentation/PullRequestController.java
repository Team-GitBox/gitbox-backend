package com.khu.gitbox.domain.pullRequest.presentation;

import com.khu.gitbox.auth.provider.JwtTokenProvider;
import com.khu.gitbox.common.response.ApiResponse;
import com.khu.gitbox.domain.pullRequest.application.PullRequestService;
import com.khu.gitbox.domain.pullRequest.presentation.dto.PullRequestCommentCreateRequest;
import com.khu.gitbox.domain.pullRequest.presentation.dto.PullRequestDto;
import com.khu.gitbox.util.SecurityContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files/{fileId}")
public class PullRequestController {

    private final PullRequestService pullRequestService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/pr")
    public ApiResponse<PullRequestDto> infoPullRequest(@PathVariable Long fileId) {
        PullRequestDto pullRequestDto = pullRequestService.infoPullRequest(fileId);

        return ApiResponse.ok(pullRequestDto);
    }

    @PostMapping("/pr")
    public ApiResponse<Boolean> createPullRequestComment(
            @RequestBody PullRequestCommentCreateRequest pullRequestCommentCreateRequest,
            @PathVariable Long fileId) {

        Long reviewerId = SecurityContextUtil.getCurrentMemberId();
        pullRequestService.isApprovedPullRequest(pullRequestCommentCreateRequest, reviewerId, fileId);

        return ApiResponse.created(pullRequestCommentCreateRequest.getIsApproved());
    }

}
