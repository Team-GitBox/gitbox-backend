package com.khu.gitbox.domain.pullRequest.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khu.gitbox.domain.pullRequest.entity.PullRequest;

public interface PullRequestRepository extends JpaRepository<PullRequest, Long> {
	Optional<PullRequest> findByFileId(Long fileId);
}
