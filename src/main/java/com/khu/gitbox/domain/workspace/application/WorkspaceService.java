package com.khu.gitbox.domain.workspace.application;

import com.khu.gitbox.domain.workspace.entity.Workspace;
import com.khu.gitbox.domain.workspace.presentation.dto.WorkspaceDetail;

import java.util.List;

public interface WorkspaceService {
    Workspace findById(Long name);

    //    WorkspaceMember findByEmail(String email);
    void deleteWorkspaces(Long id);

    void deleteMembers(List<Long> memberIds);

    void addMembers(List<String> memberIds, Long workspaceId);// 추가된 메소드

    WorkspaceDetail findByMemberIdAndWorkspaceId(Long workspaceId, Long memberId);

}

