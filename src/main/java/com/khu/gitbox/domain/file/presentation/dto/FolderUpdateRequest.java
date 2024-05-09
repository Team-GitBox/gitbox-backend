package com.khu.gitbox.domain.file.presentation.dto;

public record FolderUpdateRequest(String name, Long parentFolderId) {
}
