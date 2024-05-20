package com.khu.gitbox.domain.file.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khu.gitbox.domain.file.entity.Folder;

public interface FolderRepository extends JpaRepository<Folder, Long> {
	Optional<Folder> findByIdAndWorkspaceId(Long folderId, Long workspaceId);

	List<Folder> findAllByParentFolderId(Long parentFolderId);

	Optional<Folder> findByParentFolderIdAndName(Long parentFolderId, String folderName);
}
