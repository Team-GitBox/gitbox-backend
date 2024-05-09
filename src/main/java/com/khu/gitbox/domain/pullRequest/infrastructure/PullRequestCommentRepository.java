package com.khu.gitbox.domain.pullRequest.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khu.gitbox.domain.pullRequest.entity.PullRequestComment;

import java.util.List;
import java.util.Optional;

public interface PullRequestCommentRepository extends JpaRepository<PullRequestComment, Long> {

    Optional<List<PullRequestComment>> findAllByPullRequestId(Long pullRequestId);



}
