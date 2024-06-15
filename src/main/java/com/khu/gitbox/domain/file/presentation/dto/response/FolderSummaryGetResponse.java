package com.khu.gitbox.domain.file.presentation.dto.response;

import com.khu.gitbox.domain.file.entity.Folder;
import com.khu.gitbox.util.DateTimeUtil;

public record FolderSummaryGetResponse(
        Long id,
        String name,
        Long parentFolderId,
        Long workspaceId,
        String createdAt,
        String updatedAt
) {
    public static FolderSummaryGetResponse of(Folder folder) {
        String createdAt = DateTimeUtil.formatLocalDateTime(folder.getCreatedAt());
        String updatedAt = DateTimeUtil.formatLocalDateTime(folder.getUpdatedAt());
        return new FolderSummaryGetResponse(
                folder.getId(),
                folder.getName(),
                folder.getParentFolderId(),
                folder.getWorkspaceId(),
                createdAt,
                updatedAt
        );
    }
}
