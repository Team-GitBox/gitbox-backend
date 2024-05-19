package com.khu.gitbox.domain.file.presentation.dto.response;

import java.util.List;

import com.khu.gitbox.domain.file.entity.Folder;

public record FolderGetResponse(
	Long id,
	String name,
	Long parentFolderId,
	Long workspaceId,
	List<FileGetResponse> files
) {
	public static FolderGetResponse of(Folder folder, List<FileGetResponse> files) {
		return new FolderGetResponse(
			folder.getId(),
			folder.getName(),
			folder.getParentFolderId(),
			folder.getWorkspaceId(),
			files
		);
	}
}
