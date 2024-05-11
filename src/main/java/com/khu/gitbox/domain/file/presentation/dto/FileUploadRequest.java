package com.khu.gitbox.domain.file.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record FileUploadRequest(
	@NotNull
	Long folderId
) {
}
