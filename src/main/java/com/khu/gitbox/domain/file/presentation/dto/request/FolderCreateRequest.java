package com.khu.gitbox.domain.file.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FolderCreateRequest(
        @NotBlank
        String name,

        @NotNull
        Long parentFolderId) {
}
