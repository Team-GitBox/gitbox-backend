package com.khu.gitbox.domain.pullRequest.application;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.khu.gitbox.common.exception.CustomException;
import com.khu.gitbox.domain.file.entity.File;
import com.khu.gitbox.domain.file.entity.FileStatus;
import com.khu.gitbox.domain.file.infrastructure.FileRepository;
import com.khu.gitbox.domain.member.entity.Member;
import com.khu.gitbox.domain.member.infrastructure.MemberRepository;
import com.khu.gitbox.domain.pullRequest.entity.PullRequest;
import com.khu.gitbox.domain.pullRequest.entity.PullRequestComment;
import com.khu.gitbox.domain.pullRequest.infrastructure.PullRequestCommentRepository;
import com.khu.gitbox.domain.pullRequest.infrastructure.PullRequestRepository;
import com.khu.gitbox.domain.pullRequest.presentation.dto.PullRequestCommentDto;
import com.khu.gitbox.domain.pullRequest.presentation.dto.PullRequestDto;
import com.khu.gitbox.domain.workspace.entity.WorkspaceMember;
import com.khu.gitbox.domain.workspace.infrastructure.WorkspaceMemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PullRequestService {

	private final PullRequestRepository pullRequestRepository;
	private final PullRequestCommentRepository pullRequestCommentRepository;
	private final MemberRepository memberRepository;
	private final FileRepository fileRepository;
	private final WorkspaceMemberRepository workspaceMemberRepository;

	public PullRequestDto infoPullRequest(Long fileId) {

		PullRequest pullRequest = pullRequestRepository.findByFileId(fileId).orElseThrow(() -> {
			throw new CustomException(HttpStatus.NOT_FOUND, "pull-request가 존재하지 않습니다. 해당 파일을 다시 확인해주세요");
		});
		Member writer = memberRepository.findById(pullRequest.getWriterId()).orElseThrow(() -> {
			throw new CustomException(HttpStatus.NOT_FOUND, "작성자가 존재하지 않습니다.");
		});
		File file = fileRepository.findById(fileId).orElseThrow(() -> {
			throw new CustomException(HttpStatus.NOT_FOUND, "찾는 파일이 존재하지 않습니다.");
		});

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
			pullRequestDto.setCommentDtoList(commentList);
		}

		return pullRequestDto;

	}

	public void isApprovedPullRequest(PullRequestCommentDto pullRequestCommentDto, Long reviewerId, Long fileId) {

		PullRequest pullRequest = pullRequestRepository.findByFileId(fileId).orElseThrow(() -> {
			throw new CustomException(HttpStatus.BAD_REQUEST, "현재 보낸 파일(fileId)을 찾을 수 없습니다.");
		});

		// comment 내용 저장
		PullRequestComment pullRequestComment = new PullRequestComment(
			pullRequestCommentDto.getComment(),
			pullRequestCommentDto.getIsApproved(),
			reviewerId,
			pullRequest.getId()
		);

		pullRequestCommentRepository.save(pullRequestComment);

		List<PullRequestComment> responsers = pullRequestCommentRepository.findAllByPullRequestId(
			pullRequest.getId()).get();
		File file = fileRepository.findById(fileId).get();

		List<WorkspaceMember> members = workspaceMemberRepository.findByWorkspaceId(file.getWorkspaceId());

		if (members.size() - 1 == responsers.size()) {

			int trueCount = 0;
			int falseCount = 0;

			for (PullRequestComment responser : responsers) {
				if (responser.getIsApproved())
					trueCount++;
				else
					falseCount++;
			}

			if (trueCount > falseCount) {
				file.updateStatus(FileStatus.APPROVED);
			} else {
				file.updateStatus(FileStatus.REJECTED);
			}

			fileRepository.save(file);
		}
	}

}
