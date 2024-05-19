package com.khu.gitbox.domain.file.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FolderUpdateRequest(
	@NotBlank
	String name,

	Long parentFolderId) {
}
