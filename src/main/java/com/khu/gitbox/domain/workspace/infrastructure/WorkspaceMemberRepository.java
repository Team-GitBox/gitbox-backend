package com.khu.gitbox.domain.workspace.infrastructure;

import com.khu.gitbox.domain.workspace.entity.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, Long> {
    Optional<WorkspaceMember> findByMemberIdAndWorkspaceId(Long memberId, Long workspaceId);

    List<WorkspaceMember> findByWorkspaceId(Long workspaceId);

    void deleteByWorkspaceId(Long workspaceId);

    void deleteByMemberId(Long memberId);
}
