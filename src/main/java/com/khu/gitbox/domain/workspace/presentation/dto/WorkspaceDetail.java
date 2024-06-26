package com.khu.gitbox.domain.workspace.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceDetail {
    private String workspaceName;
    private Long rootFolderId;
    private OwnerInfo ownerInfo;
    private List<MemberInfo> memberInfo;
    private Long maxStorage;
    private Long usedStorage;
    private Map<String, Long> usedStorageByFileType;
}
