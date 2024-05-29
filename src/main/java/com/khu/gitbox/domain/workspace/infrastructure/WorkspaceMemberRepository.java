package com.khu.gitbox.domain.workspace.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khu.gitbox.domain.workspace.entity.WorkspaceMember;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, Long> {
	Optional<WorkspaceMember> findByWorkspaceIdAndMemberId(Long workspaceId, Long memberId);

	List<WorkspaceMember> findByWorkspaceId(Long workspaceId);

	void deleteByWorkspaceId(Long workspaceId);

	void deleteByWorkspaceIdAndMemberId(Long workspaceId, Long memberId);

	List<WorkspaceMember> findByMemberId(Long memberId);
}
