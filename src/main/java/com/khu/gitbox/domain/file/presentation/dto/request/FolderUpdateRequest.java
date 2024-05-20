package com.khu.gitbox.domain.file.presentation.dto.request;

import jakarta.validation.constraints.Size;

public record FolderUpdateRequest(
	@Size(min = 1)
	String name,

	Long parentFolderId) {
}
