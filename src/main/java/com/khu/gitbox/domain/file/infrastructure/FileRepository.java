package com.khu.gitbox.domain.file.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.khu.gitbox.domain.file.entity.File;
import com.khu.gitbox.domain.file.entity.FileTag;

public interface FileRepository extends JpaRepository<File, Long> {
	void deleteByFolderId(Long folderId);

	@Query("SELECT f FROM File f WHERE f.parentFileId = :parentFileId AND f.status = 'PENDING' AND f.isDeleted = FALSE")
	Optional<File> findPendingFile(Long parentFileId);

	@Query("SELECT f FROM File f WHERE f.rootFileId = :rootFileId AND f.isDeleted = FALSE ORDER BY f.version DESC")
	List<File> findAllByRootFileId(Long rootFileId);

	@Query("SELECT f FROM File f WHERE f.folderId = :folderId AND f.isLatest = true AND f.isDeleted = FALSE ORDER BY f.name ASC")
	List<File> findLatestByFolderId(Long folderId);

	@Query("SELECT f FROM File f WHERE f.workspaceId = :workspaceId AND f.isDeleted = FALSE ORDER BY f.name ASC")
	List<File> findAllByWorkspaceId(Long workspaceId);

	@Query("SELECT f FROM File f WHERE f.workspaceId = :workspaceId AND f.tag = :tag AND f.isDeleted = FALSE ORDER BY f.name ASC")
	List<File> findAllByTag(Long workspaceId, FileTag tag);

	@Query("SELECT f FROM File f WHERE f.folderId = :folderId AND f.name = :name AND f.isDeleted = FALSE")
	Optional<File> findByFolderIdAndName(Long folderId, String name);
}
