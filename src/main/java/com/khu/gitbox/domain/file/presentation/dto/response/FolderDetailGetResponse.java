package com.khu.gitbox.domain.file.presentation.dto.response;

import com.khu.gitbox.domain.file.entity.Folder;

import java.time.LocalDateTime;
import java.util.List;

public record FolderDetailGetResponse(
        Long id,
        String name,
        Long parentFolderId,
        Long workspaceId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<FolderSummaryGetResponse> folders,
        List<FileGetResponse> files
) {
    public static FolderDetailGetResponse of(
            Folder folder,
            List<FolderSummaryGetResponse> folders,
            List<FileGetResponse> files) {
        return new FolderDetailGetResponse(
                folder.getId(),
                folder.getName(),
                folder.getParentFolderId(),
                folder.getWorkspaceId(),
                folder.getCreatedAt(),
                folder.getUpdatedAt(),
                folders,
                files
        );
    }
}
