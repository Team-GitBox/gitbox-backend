package com.khu.gitbox.domain.file.presentation.dto;

public record FileCreateRequest(
	Long workspaceId,
	Long folderId
) {
}
