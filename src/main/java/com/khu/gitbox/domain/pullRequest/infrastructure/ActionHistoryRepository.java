package com.khu.gitbox.domain.pullRequest.infrastructure;

import com.khu.gitbox.domain.pullRequest.entity.ActionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionHistoryRepository extends JpaRepository<ActionHistory, Long> {

    findAllByWorkspaceId(Long workspaceId)

}
