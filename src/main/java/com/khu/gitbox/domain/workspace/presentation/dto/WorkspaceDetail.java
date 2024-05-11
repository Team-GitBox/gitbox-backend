package com.khu.gitbox.domain.workspace.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class WorkspaceDetail {
    private Long workspaceId;
    private String workspaceName; // 워크스페이스 이름
    private OwnerInfo ownerInfo;
    private List<MemberInfo> memberInfo;
    private Long maxStorage;
    private Long usedStorage;
}
