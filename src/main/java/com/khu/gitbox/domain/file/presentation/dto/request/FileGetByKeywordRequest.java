package com.khu.gitbox.domain.file.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FileGetByKeywordRequest(
        @NotNull(message = "workspaceId는 필수입니다.")
        Long workspaceId,

        @NotBlank(message = "keyword는 필수입니다.")
        String keyword
) {
}
