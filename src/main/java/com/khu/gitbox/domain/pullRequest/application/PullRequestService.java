package com.khu.gitbox.domain.pullRequest.application;

import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.domain.file.application.FileService;
import com.khu.gitbox.domain.file.entity.File;
import com.khu.gitbox.domain.member.entity.Member;
import com.khu.gitbox.domain.member.infrastructure.MemberRepository;
import com.khu.gitbox.domain.pullRequest.entity.PullRequest;
import com.khu.gitbox.domain.pullRequest.entity.PullRequestComment;
import com.khu.gitbox.domain.pullRequest.infrastructure.PullRequestCommentRepository;
import com.khu.gitbox.domain.pullRequest.infrastructure.PullRequestRepository;
import com.khu.gitbox.domain.pullRequest.presentation.dto.PullRequestCommentCreateRequest;
import com.khu.gitbox.domain.pullRequest.presentation.dto.PullRequestGetResponse;
import com.khu.gitbox.domain.workspace.entity.WorkspaceMember;
import com.khu.gitbox.domain.workspace.infrastructure.WorkspaceMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PullRequestService {

    private final PullRequestRepository pullRequestRepository;
    private final PullRequestCommentRepository pullRequestCommentRepository;
    private final MemberRepository memberRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final FileService fileService;

    public PullRequestGetResponse getPullRequest(Long pullRequestId) {
        PullRequest pullRequest = pullRequestRepository.findById(pullRequestId).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "해당 pull-request를 찾을 수 없습니다."));
        Member writer = memberRepository.findById(pullRequest.getWriterId()).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "작성자가 존재하지 않습니다."));
        File file = fileService.findFileById(pullRequest.getFileId());

        List<PullRequestComment> comments = pullRequestCommentRepository.findAllByPullRequestId(pullRequest.getId());

        return PullRequestGetResponse.builder()
                .title(pullRequest.getTitle())
                .message(pullRequest.getMessage())
                .writer(writer.getEmail())
                .parentFileId(file.getParentFileId())
                .fileId(file.getId())
                .fileUrl(file.getUrl())
                .comments(comments)
                .build();
    }

    public void createComment(PullRequestCommentCreateRequest request, Long reviewerId, Long pullRequestId) {
        PullRequest pullRequest = pullRequestRepository.findById(pullRequestId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "해당 pull-request를 찾을 수 없습니다."));
        File file = fileService.findFileById(pullRequest.getFileId());
        if (file.getWriterId().equals(reviewerId)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "본인이 작성한 파일에 대한 pull-request는 승인할 수 없습니다.");
        }

        PullRequestComment pullRequestComment = new PullRequestComment(
                request.getComment(),
                request.getIsApproved(),
                reviewerId,
                pullRequest.getId()
        );

        pullRequestCommentRepository.save(pullRequestComment);

        List<PullRequestComment> comments = pullRequestCommentRepository.findAllByPullRequestId(pullRequest.getId());
        List<WorkspaceMember> members = workspaceMemberRepository.findByWorkspaceId(file.getWorkspaceId());

        if (members.size() - 1 == comments.size()) {
            log.info("모든 멤버가 리뷰를 완료했습니다.");
            int trueCount = 0;
            int falseCount = 0;

            for (PullRequestComment comment : comments) {
                if (comment.isApproved())
                    trueCount++;
                else
                    falseCount++;
            }
            log.info("trueCount : {}", trueCount);

            File parentFile = fileService.findFileById(file.getParentFileId());
            if (trueCount > falseCount) {
                file.approve(parentFile);
            } else {
                file.reject(parentFile);
            }
        }
    }
}
