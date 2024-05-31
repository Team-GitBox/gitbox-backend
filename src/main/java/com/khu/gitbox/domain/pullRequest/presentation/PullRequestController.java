package com.khu.gitbox.domain.pullRequest.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khu.gitbox.auth.provider.JwtTokenProvider;
import com.khu.gitbox.common.response.ApiResponse;
import com.khu.gitbox.domain.pullRequest.application.PullRequestService;
import com.khu.gitbox.domain.pullRequest.presentation.dto.PullRequestCommentCreateRequest;
import com.khu.gitbox.domain.pullRequest.presentation.dto.PullRequestDto;
import com.khu.gitbox.util.SecurityContextUtil;

import lombok.RequiredArgsConstructor;

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
