package com.khu.gitbox.domain.workspace.infrastructure;

import com.khu.gitbox.domain.workspace.entity.ActionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionHistoryRepository extends JpaRepository<ActionHistory, Long> {
}
