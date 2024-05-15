package com.khu.gitbox.domain.workspace.infrastructure;

import com.khu.gitbox.domain.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    Optional<Workspace> findByOwnerId(Long ownerId);

    Boolean existsByOwnerId(Long ownerId);
}

