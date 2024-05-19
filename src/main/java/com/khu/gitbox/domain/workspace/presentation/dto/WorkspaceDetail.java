package com.khu.gitbox.domain.workspace.presentation.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
