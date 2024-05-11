package com.khu.gitbox.domain.action.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.khu.gitbox.domain.action.entity.ActionHistory;

public interface ActionHistoryRepository extends JpaRepository<ActionHistory, Long> {
	Page<ActionHistory> findAllByWorkspaceId(Long workspaceId, Pageable pageable);
}
