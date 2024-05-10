package com.khu.gitbox.domain.workspace.infrastructure;

import com.khu.gitbox.domain.workspace.entity.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, Long> {

    //Optional<WorkspaceMember> findByEmail(String email);
}