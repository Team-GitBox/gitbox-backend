package com.khu.gitbox.domain.file.presentation.dto.response;

public record FolderGetResponse(
	Long id,
	String name,
	Long parentFolderId,
	Long workspaceId
) {
}
