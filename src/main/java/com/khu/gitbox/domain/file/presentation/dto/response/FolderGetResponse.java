package com.khu.gitbox.domain.file.presentation.dto.response;

import com.khu.gitbox.domain.file.entity.Folder;

public record FolderGetResponse(
	Long id,
	String name,
	Long parentFolderId,
	Long workspaceId
) {
	public static FolderGetResponse of(Folder folder) {
		return new FolderGetResponse(
			folder.getId(),
			folder.getName(),
			folder.getParentFolderId(),
			folder.getWorkspaceId()
		);
	}
}
