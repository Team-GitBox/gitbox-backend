package com.khu.gitbox.domain.pullRequest.application;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.domain.file.entity.File;
import com.khu.gitbox.domain.file.entity.FileStatus;
import com.khu.gitbox.domain.file.infrastructure.FileRepository;
import com.khu.gitbox.domain.member.entity.Member;
import com.khu.gitbox.domain.member.infrastructure.MemberRepository;
import com.khu.gitbox.domain.pullRequest.entity.ActionHistory;
import com.khu.gitbox.domain.pullRequest.entity.PullRequest;
import com.khu.gitbox.domain.pullRequest.entity.PullRequestComment;
import com.khu.gitbox.domain.pullRequest.infrastructure.ActionHistoryRepository;
import com.khu.gitbox.domain.pullRequest.infrastructure.PullRequestCommentRepository;
import com.khu.gitbox.domain.pullRequest.infrastructure.PullRequestRepository;
import com.khu.gitbox.domain.pullRequest.presentation.dto.ActionHistoryDto;
import com.khu.gitbox.domain.pullRequest.presentation.dto.PullRequestCommentDto;
import com.khu.gitbox.domain.pullRequest.presentation.dto.PullRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PullRequestService {

	private final PullRequestRepository pullRequestRepository;
	private final PullRequestCommentRepository pullRequestCommentRepository;
	private final MemberRepository memberRepository;
	private final FileRepository fileRepository;
	private final ActionHistoryRepository actionHistoryRepository;

	public PullRequestDto infoPullRequest(Long fileId) {

		PullRequest pullRequest = pullRequestRepository.findByFileId(fileId).orElseThrow(() -> {
			throw new CustomException(HttpStatus.NOT_FOUND, "pull request is null. check your file again.");
		});
		Member writer = memberRepository.findById(pullRequest.getWriterId()).orElseThrow(() -> {
			throw new CustomException(HttpStatus.NOT_FOUND, "Writer is not in Member database.");
		});
		File file = fileRepository.findById(fileId).orElseThrow(() -> {
			throw new CustomException(HttpStatus.NOT_FOUND, "File is not match in file database.");
		});

		List<PullRequestComment> commentList = pullRequestCommentRepository.findAllByPullRequestId(pullRequest.getId())
			.get();

		PullRequestDto pullRequestDto = PullRequestDto.builder()
			.title(pullRequest.getTitle())
			.message(pullRequest.getMessage())
			.writer(writer.getEmail())
			.fileUrl(file.getUrl())
			.build();

		if (!commentList.isEmpty()) {
			pullRequestDto.setCommentDtoList(commentList);
		}

		return pullRequestDto;

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

		Optional<List<PullRequestComment>> responsers = pullRequestCommentRepository.findAllByPullRequestId(
			pullRequest.get().getId());

		// 저장했는데 해당 워크스페이스 수 -1 과 코멘트를 남긴 사람의 수가 같다면 true와 false를 비교
		if (responsers.get().size() == 3/*동원이형이랑 합치면 수정해야함. workspace의 수 - 1*/) {
			Optional<File> file = fileRepository.findById(fileId);
			int trueCount = 0;
			int falseCount = 0;

			for (PullRequestComment responser : responsers.get()) {
				if (responser.getIsApproved())
					trueCount++;
				else
					falseCount++;
			}

			if (trueCount > falseCount) {
				file.get().updateStatus(FileStatus.APPROVED);
			} else {
				file.get().updateStatus(FileStatus.REJECTED);
			}
			fileRepository.save(file.get());
		}
	}

	public Page<ActionHistoryDto> getActionHistoryList(int page, Pageable pageable, Long workspaceId) {
		Page<ActionHistory> allByWorkspaceId = actionHistoryRepository.findAllByWorkspaceId(workspaceId, pageable);

		Page<ActionHistoryDto> actionHistoryDtos = null;

		return actionHistoryDtos;
	}
}
