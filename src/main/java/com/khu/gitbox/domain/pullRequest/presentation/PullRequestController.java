package com.khu.gitbox.domain.pullRequest.presentation;

import com.khu.gitbox.auth.provider.JwtTokenProvider;
import com.khu.gitbox.common.response.ApiResponse;
import com.khu.gitbox.domain.pullRequest.application.PullRequestService;
import com.khu.gitbox.domain.pullRequest.presentation.dto.ActionHistoryDto;
import com.khu.gitbox.domain.pullRequest.presentation.dto.PullRequestCommentDto;
import com.khu.gitbox.domain.pullRequest.presentation.dto.PullRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{fileId}")
public class PullRequestController {

    private final PullRequestService pullRequestService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/pr")
    public ResponseEntity<ApiResponse<PullRequestDto>> infoPullRequest(@PathVariable Long fileId) {
        PullRequestDto pullRequestDto = pullRequestService.infoPullRequest(fileId);

        return ResponseEntity.ok(ApiResponse.ok(pullRequestDto));
    }

    @PostMapping("/pr")
    public ResponseEntity<ApiResponse<Boolean>> checkPullRequestComment(
            @RequestHeader("Cookie") String cookie,
            @RequestBody PullRequestCommentDto pullRequestCommentDto,
            @PathVariable Long fileId) {

        Long id = jwtTokenProvider.getId(cookie.substring(12));

        pullRequestService.isApprovedPullRequest(pullRequestCommentDto, id, fileId);

        return ResponseEntity.ok(ApiResponse.created(pullRequestCommentDto.getIsApproved()));
    }

    // workspace메인화면에서 history버튼?같이 클릭해야 알 수 있을 듯?
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<Page>> history(
            @PathVariable Long fileId,
            @RequestParam int page,
            @PageableDefault(page = 0, size = 5)Pageable pageable) {

        Page<ActionHistoryDto> actionHistoryList = pullRequestService.getActionHistoryList(page, pageable);

        return ResponseEntity.ok(ApiResponse.ok(actionHistoryList));

    }

}
