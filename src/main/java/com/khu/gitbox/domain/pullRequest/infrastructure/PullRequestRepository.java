package com.khu.gitbox.domain.pullRequest.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khu.gitbox.domain.pullRequest.entity.PullRequest;

public interface PullRequestRepository extends JpaRepository<PullRequest, Long> {
}
