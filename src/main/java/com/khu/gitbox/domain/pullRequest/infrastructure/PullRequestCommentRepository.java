package com.khu.gitbox.domain.pullRequest.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khu.gitbox.domain.pullRequest.entity.PullRequestComment;

public interface PullRequestCommentRepository extends JpaRepository<PullRequestComment, Long> {
}
