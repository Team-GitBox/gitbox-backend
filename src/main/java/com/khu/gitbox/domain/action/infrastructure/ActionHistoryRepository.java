package com.khu.gitbox.domain.action.infrastructure;

import com.khu.gitbox.domain.action.entity.ActionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActionHistoryRepository extends JpaRepository<ActionHistory, Long> {
    // Page<ActionHistory> findAllByWorkspaceId(Long workspaceId, Pageable pageable);
    // Long countByWorkspaceId(Long workspaceId);
    @Query("SELECT ah FROM ActionHistory ah WHERE ah.workspaceId = :workspaceId ORDER BY ah.createdAt DESC")
    List<ActionHistory> findAllByWorkspaceId(Long workspaceId);
}
