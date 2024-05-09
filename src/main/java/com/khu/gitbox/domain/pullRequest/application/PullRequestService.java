package com.khu.gitbox.domain.pullRequest.application;

import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.domain.file.entity.File;
import com.khu.gitbox.domain.file.infrastructure.FileRepository;
import com.khu.gitbox.domain.member.entity.Member;
import com.khu.gitbox.domain.member.infrastructure.MemberRepository;
import com.khu.gitbox.domain.pullRequest.entity.PullRequest;
import com.khu.gitbox.domain.pullRequest.entity.PullRequestComment;
import com.khu.gitbox.domain.pullRequest.infrastructure.PullRequestCommentRepository;
import com.khu.gitbox.domain.pullRequest.infrastructure.PullRequestRepository;
import com.khu.gitbox.domain.pullRequest.presentation.PullRequestController;
import com.khu.gitbox.domain.pullRequest.presentation.dto.PullRequestCommentDto;
import com.khu.gitbox.domain.pullRequest.presentation.dto.PullRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PullRequestService {

    private final PullRequestRepository pullRequestRepository;
    private final PullRequestCommentRepository pullRequestCommentRepository;
    private final MemberRepository memberRepository;
    private final FileRepository fileRepository;

    public PullRequestDto infoPullRequest(Long fileId) {

        Optional<PullRequest> pullRequest = pullRequestRepository.findByFileId(fileId);
        Optional<Member> writer = memberRepository.findById(pullRequest.get().getWriterId());
        Optional<File> file = fileRepository.findById(fileId);

        if(pullRequest.isPresent()) {
            Optional<List<PullRequestComment>> commentList = pullRequestCommentRepository.findAllByPullRequestId(pullRequest.get().getId());

            PullRequestDto pullRequestDto = PullRequestDto.builder()
                    .title(pullRequest.get().getTitle())
                    .message(pullRequest.get().getMessage())
                    .writer(writer.get().getEmail())
                    .fileUrl(file.get().getUrl())
                    .build();

            if(commentList.isPresent()) {
                pullRequestDto.builder().commentDtoList(commentList.get());
            }

            return pullRequestDto;

        }

        throw new CustomException(HttpStatus.NOT_FOUND, "this file has no pull request now.");
    }

    public void isApprovedPullRequest(PullRequestCommentDto pullRequestCommentDto, Long reviewerId, Long fileId) {

        Optional<PullRequest> pullRequest = pullRequestRepository.findByFileId(fileId);

        // comment 내용 저장
        PullRequestComment pullRequestComment = new PullRequestComment(
                pullRequestCommentDto.getComment(),
                pullRequestCommentDto.getIsApproved(),
                reviewerId,
                pullRequest.get().getId()
        );

        pullRequestCommentRepository.save(pullRequestComment);

        Optional<List<PullRequestComment>> responsers = pullRequestCommentRepository.findAllByPullRequestId(pullRequest.get().getId());

        // 저장했는데 해당 워크스페이스 수 -1 과 코멘트를 남긴 사람의 수가 같다면 true와 false를 비교
        if(responsers.get().size() == 3/*동원이형이랑 합치면 수정해야함. workspace의 수 - 1*/) {
            Optional<File> file = fileRepository.findById(fileId);
            int trueCount = 0;
            int falseCount = 0;

            for(PullRequestComment responser : responsers.get()) {
                if(responser.getIsApproved()) trueCount++;
                else falseCount++;
            }

            if(trueCount > falseCount) {
                file.get().builder().isApproved(true);
            }
            else {
                file.get().builder().isApproved(false);
            }

            fileRepository.save(file.get());
        }
    }
}
