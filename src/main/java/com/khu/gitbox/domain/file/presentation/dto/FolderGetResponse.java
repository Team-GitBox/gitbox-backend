package com.khu.gitbox.domain.file.presentation.dto;

public record FolderGetResponse(
	Long id,
	String name,
	Long parentFolderId,
	Long workspaceId
) {
}
