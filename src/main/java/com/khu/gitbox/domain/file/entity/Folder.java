package com.khu.gitbox.domain.file.entity;

import com.khu.gitbox.common.BaseEntity;

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
@Table(name = "folder")
public class Folder extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "parent_folder_id")
	private Long parentFolderId;

	@Column(name = "workspace_id", nullable = false)
	private Long workspaceId;

	@Builder
	Folder(String name, Long parentFolderId, Long workspaceId) {
		this.name = name;
		this.parentFolderId = parentFolderId;
		this.workspaceId = workspaceId;
	}

	public void updateFolder(String name, Long parentFolderId) {
		this.name = name;
		this.parentFolderId = parentFolderId != null ? parentFolderId : this.parentFolderId;
	}
}
