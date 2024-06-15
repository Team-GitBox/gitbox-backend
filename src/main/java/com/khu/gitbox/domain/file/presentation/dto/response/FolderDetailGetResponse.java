package com.khu.gitbox.domain.file.presentation.dto.response;

import com.khu.gitbox.domain.file.entity.Folder;
import com.khu.gitbox.util.DateTimeUtil;

import java.util.List;

public record FolderDetailGetResponse(
        Long id,
        String name,
        Long parentFolderId,
        Long workspaceId,
        String createdAt,
        String updatedAt,
        List<FolderSummaryGetResponse> folders,
        List<FileGetResponse> files
) {
    public static FolderDetailGetResponse of(
            Folder folder,
            List<FolderSummaryGetResponse> folders,
            List<FileGetResponse> files) {
        String createdAt = DateTimeUtil.formatLocalDateTime(folder.getCreatedAt());
        String updatedAt = DateTimeUtil.formatLocalDateTime(folder.getUpdatedAt());
        return new FolderDetailGetResponse(
                folder.getId(),
                folder.getName(),
                folder.getParentFolderId(),
                folder.getWorkspaceId(),
                createdAt,
                updatedAt,
                folders,
                files
        );
    }
}
