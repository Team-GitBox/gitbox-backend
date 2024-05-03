package com.khu.gitbox.domain.workspace.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khu.gitbox.domain.workspace.entity.ActionHistory;

public interface ActionHistoryRepository extends JpaRepository<ActionHistory, Long> {
}
