package com.khu.gitbox.domain.file.presentation.dto.response;

import com.khu.gitbox.domain.file.entity.File;
import com.khu.gitbox.domain.file.entity.FileStatus;
import com.khu.gitbox.domain.file.entity.FileType;

public record FileGetResponse(
	Long id,
	String name,
	Long size,
	String url,
	FileType type,
	FileStatus status,
	Long version,
	boolean isLatest,
	Long writerId,
	Long workspaceId,
	Long folderId,
	Long parentFileId,
	Long rootFileId
) {
	public static FileGetResponse of(File file) {
		return new FileGetResponse(
			file.getId(),
			file.getName(),
			file.getSize(),
			file.getUrl(),
			file.getType(),
			file.getStatus(),
			file.getVersion(),
			file.isLatest(),
			file.getWriterId(),
			file.getWorkspaceId(),
			file.getFolderId(),
			file.getParentFileId(),
			file.getRootFileId()
		);
	}
}
