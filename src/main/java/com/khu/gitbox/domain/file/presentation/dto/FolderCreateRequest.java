package com.khu.gitbox.domain.file.presentation.dto;

public record FolderCreateRequest(String name, Long parentFolderId) {
}
