package com.khu.gitbox.domain.file.presentation.dto;

import com.khu.gitbox.domain.file.entity.FileTag;

public record FileGetRequest(
	Long workspaceId,
	FileTag tag
) {
}
