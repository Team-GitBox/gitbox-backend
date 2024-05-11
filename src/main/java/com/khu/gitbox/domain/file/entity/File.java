package com.khu.gitbox.domain.file.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "file")
public class File {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "size", nullable = false)
	private Long size;

	@Column(name = "url", nullable = false)
	private String url;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private FileType type;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private FileStatus status;

	@Column(name = "is_latest", nullable = false)
	private boolean isLatest;

	@Column(name = "writer_id", nullable = false)
	private Long writerId;

	@Column(name = "workspace_id", nullable = false)
	private Long workspaceId;

	@Column(name = "folder_id", nullable = false)
	private Long folderId;

	@Column(name = "parent_file_id")
	private Long parentFileId;

	@Column(name = "root_file_id", nullable = false)
	private Long rootFileId;

	@Builder
	File(
		String name,
		Long size,
		String url,
		FileType type,
		FileStatus status,
		boolean isLatest,
		Long writerId,
		Long workspaceId,
		Long folderId,
		Long parentFileId,
		Long rootFileId
	) {
		this.name = name;
		this.size = size;
		this.url = url;
		this.type = type;
		this.status = status;
		this.isLatest = isLatest;
		this.writerId = writerId;
		this.workspaceId = workspaceId;
		this.folderId = folderId;
		this.parentFileId = parentFileId;
		this.rootFileId = rootFileId;
	}

	public void delete() {
		this.status = FileStatus.DELETED;
	}
}
