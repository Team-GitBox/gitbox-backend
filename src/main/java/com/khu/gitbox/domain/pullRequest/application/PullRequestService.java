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
import com.khu.gitbox.domain.pullRequest.presentation.dto.PullRequestDto;
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

    public PullRequestDto infoPullRequest(Long fileId) {
        PullRequest pullRequest = pullRequestRepository.findByFileId(fileId).orElseThrow(() -> {
            throw new CustomException(HttpStatus.NOT_FOUND, "pull-request가 존재하지 않습니다. 해당 파일을 다시 확인해주세요");
        });

        Member writer = memberRepository.findById(pullRequest.getWriterId()).orElseThrow(() -> {
            throw new CustomException(HttpStatus.NOT_FOUND, "작성자가 존재하지 않습니다.");
        });
        File file = fileService.findFileById(fileId);

        List<PullRequestComment> commentList = pullRequestCommentRepository.findAllByPullRequestId(pullRequest.getId())
                .orElseThrow(() -> {
                    throw new CustomException(HttpStatus.NOT_FOUND, "코멘트를 찾을 수 없습니다.");
                });

        PullRequestDto pullRequestDto = PullRequestDto.builder()
                .title(pullRequest.getTitle())
                .message(pullRequest.getMessage())
                .writer(writer.getEmail())
                .fileUrl(file.getUrl())
                .build();

        if (!commentList.isEmpty()) {
            pullRequestDto.setComments(commentList);
        }
        return pullRequestDto;
    }

    public void isApprovedPullRequest(PullRequestCommentCreateRequest pullRequestCommentCreateRequest, Long reviewerId,
                                      Long fileId) {
        File file = fileService.findFileById(fileId);

        if (file.getWriterId().equals(reviewerId)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "본인이 작성한 파일에 대한 pull-request는 승인할 수 없습니다.");
        }

        PullRequest pullRequest = pullRequestRepository.findByFileId(fileId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "현재 보낸 파일(fileId)을 찾을 수 없습니다."));

        // comment 내용 저장
        PullRequestComment pullRequestComment = new PullRequestComment(
                pullRequestCommentCreateRequest.getComment(),
                pullRequestCommentCreateRequest.getIsApproved(),
                reviewerId,
                pullRequest.getId()
        );

        pullRequestCommentRepository.save(pullRequestComment);

        List<PullRequestComment> responsers = pullRequestCommentRepository.findAllByPullRequestId(
                pullRequest.getId()).get();

        List<WorkspaceMember> members = workspaceMemberRepository.findByWorkspaceId(file.getWorkspaceId());

        if (members.size() - 1 == responsers.size()) {
            log.info("모든 멤버가 리뷰를 완료했습니다.");
            int trueCount = 0;
            int falseCount = 0;

            for (PullRequestComment responser : responsers) {
                if (responser.getIsApproved())
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
