package com.khu.gitbox.domain.file.presentation.dto.request;

import com.khu.gitbox.domain.file.entity.FileTag;

import jakarta.validation.constraints.NotNull;

public record FileGetByTagRequest(
	@NotNull(message = "workspaceId는 필수입니다.")
	Long workspaceId,
	FileTag tag
) {
}
