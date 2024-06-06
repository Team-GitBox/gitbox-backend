package com.khu.gitbox.domain.file.presentation.dto.request;

public record FileCreateRequest(
        Long workspaceId,
        Long folderId
) {
}
