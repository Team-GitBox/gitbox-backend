package com.khu.gitbox.domain.file.presentation.dto.response;

import com.khu.gitbox.domain.file.entity.Folder;

import java.time.LocalDateTime;

public record FolderSummaryGetResponse(
        Long id,
        String name,
        Long parentFolderId,
        Long workspaceId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static FolderSummaryGetResponse of(Folder folder) {
        return new FolderSummaryGetResponse(
                folder.getId(),
                folder.getName(),
                folder.getParentFolderId(),
                folder.getWorkspaceId(),
                folder.getCreatedAt(),
                folder.getUpdatedAt()
        );
    }
}
