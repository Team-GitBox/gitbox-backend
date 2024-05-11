package com.khu.gitbox.domain.pullRequest.infrastructure;

import com.khu.gitbox.domain.pullRequest.entity.ActionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionHistoryRepository extends JpaRepository<ActionHistory, Long> {

    Page<ActionHistory> findAllByWorkspaceId(Long workspaceId, Pageable pageable);

}
