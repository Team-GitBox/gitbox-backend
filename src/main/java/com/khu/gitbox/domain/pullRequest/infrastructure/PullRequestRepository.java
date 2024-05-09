package com.khu.gitbox.domain.pullRequest.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khu.gitbox.domain.pullRequest.entity.PullRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PullRequestRepository extends JpaRepository<PullRequest, Long> {

    Optional<PullRequest> findByFileId(Long fileId);

}
