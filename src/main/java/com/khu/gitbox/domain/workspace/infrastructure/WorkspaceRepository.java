package com.khu.gitbox.domain.workspace.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khu.gitbox.domain.workspace.entity.Workspace;

import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    Optional<Workspace> findById(Long id);
}

