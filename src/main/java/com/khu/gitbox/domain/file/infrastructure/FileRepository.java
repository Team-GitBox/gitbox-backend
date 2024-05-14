package com.khu.gitbox.domain.file.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.khu.gitbox.domain.file.entity.File;

public interface FileRepository extends JpaRepository<File, Long> {
	void deleteByFolderId(Long folderId);

	@Query("SELECT f FROM File f WHERE f.parentFileId = :parentFileId AND f.status = 'PENDING'")
	Optional<File> findPendingFile(Long parentFileId);

	@Query("SELECT f FROM File f WHERE f.rootFileId = :rootFileId ORDER BY f.version DESC")
	List<File> findAllByRootFileId(Long rootFileId);
}
