package com.khu.gitbox.domain.workspace.application;

import com.khu.gitbox.domain.workspace.entity.Workspace;
import com.khu.gitbox.domain.workspace.entity.WorkspaceMember;

public interface WorkspaceService {
    Workspace findById(Long name);
//    WorkspaceMember findByEmail(String email);
    void delete(Long id);
}

