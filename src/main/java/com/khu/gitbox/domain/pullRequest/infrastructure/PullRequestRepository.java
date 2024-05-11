package com.khu.gitbox.domain.pullRequest.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khu.gitbox.domain.pullRequest.entity.PullRequest;

public interface PullRequestRepository extends JpaRepository<PullRequest, Long> {
	List<PullRequest> findAllByFileId(Long fileId);
}
