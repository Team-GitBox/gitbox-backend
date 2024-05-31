package com.khu.gitbox.domain.file.presentation.dto.response;

import java.time.LocalDateTime;

import com.khu.gitbox.domain.file.entity.File;
import com.khu.gitbox.domain.file.entity.FileStatus;
import com.khu.gitbox.domain.file.entity.FileTag;
import com.khu.gitbox.domain.file.entity.FileType;

public record FileGetResponse(
	Long id,
	String name,
	Long size,
	String url,
	FileTag tag,
	FileType type,
	FileStatus status,
	Long version,
	boolean isLatest,
	Long writerId,
	Long workspaceId,
	Long folderId,
	Long parentFileId,
	Long rootFileId,
	Long pullRequestId,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static FileGetResponse of(File file) {
		return new FileGetResponse(
			file.getId(),
			file.getName(),
			file.getSize(),
			file.getUrl(),
			file.getTag(),
			file.getType(),
			file.getStatus(),
			file.getVersion(),
			file.isLatest(),
			file.getWriterId(),
			file.getWorkspaceId(),
			file.getFolderId(),
			file.getParentFileId(),
			file.getRootFileId(),
			file.getPullRequestId(),
			file.getCreatedAt(),
			file.getUpdatedAt()
		);
	}
}
