package com.khu.gitbox.domain.action.infrastructure;

import com.khu.gitbox.domain.action.entity.ActionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionHistoryRepository extends JpaRepository<ActionHistory, Long> {
    Page<ActionHistory> findAllByWorkspaceIdByOrderByIdDesc(Long workspaceId, Pageable pageable);

    Long countByWorkspaceId(Long workspaceId);
}
