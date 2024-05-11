package com.khu.gitbox.domain.file.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

	@Column(name = "type", nullable = false)
	private String type;

	@Column(name = "size", nullable = false)
	private Long size;

	@Column(name = "url", nullable = false)
	private String url;

	@Column(name = "is_latest", nullable = false)
	private boolean isLatest;

	@Column(name = "is_approved", nullable = false)
	private boolean isApproved;

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
	public File(String name, String type, Long size, String url, boolean isLatest, boolean isApproved, Long writerId, Long workspaceId, Long folderId, Long parentFileId, Long rootFileId) {
		this.name = name;
		this.type = type;
		this.size = size;
		this.url = url;
		this.isLatest = isLatest;
		this.isApproved = isApproved;
		this.writerId = writerId;
		this.workspaceId = workspaceId;
		this.folderId = folderId;
		this.parentFileId = parentFileId;
		this.rootFileId = rootFileId;
	}
}
