package com.khu.gitbox.domain.pullRequest.infrastructure;

import com.khu.gitbox.domain.pullRequest.entity.PullRequestComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PullRequestCommentRepository extends JpaRepository<PullRequestComment, Long> {

    Optional<List<PullRequestComment>> findAllByPullRequestId(Long pullRequestId);
}
