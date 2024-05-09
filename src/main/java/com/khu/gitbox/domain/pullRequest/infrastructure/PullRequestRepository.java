package com.khu.gitbox.domain.pullRequest.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khu.gitbox.domain.pullRequest.entity.PullRequest;

import java.util.List;
import java.util.Optional;

public interface PullRequestRepository extends JpaRepository<PullRequest, Long> {

    Optional<PullRequest> findByFileId(Long fileId);

}
