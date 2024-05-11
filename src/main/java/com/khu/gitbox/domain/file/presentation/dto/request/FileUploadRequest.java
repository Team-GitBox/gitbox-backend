package com.khu.gitbox.domain.file.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

public record FileUploadRequest(
	@NotNull
	Long folderId
) {
}
